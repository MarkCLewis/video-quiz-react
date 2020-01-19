package models

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.Future
import java.sql.Timestamp
import java.util.Date
import Tables._
import scala.concurrent.ExecutionContext
import shared.SharedData._

/**
 * @author mlewis
 */
object Queries {
    
  // Courses
  def coursesFor(userid: Int, db: Database)(implicit ec: ExecutionContext): Future[Seq[(CoursesRow, Int)]] = {
    db.run {
      (for {
        uca <- UserCourseAssoc
        if uca.userid === userid
        c <- Courses
        if c.courseid === uca.courseid
      } yield (c, uca.role)).result.map(_.distinct)
    }
  }
  
  def addCourse(ncd:NewCourseData, userid:Int, db: Database)(implicit ec: ExecutionContext):Unit = {
    val StudentRegex = """(\d{7})|(\w{2,8})@trinity\.edu""".r
    // Create course entry
    db.run(Courses += CoursesRow(0,ncd.code,ncd.semester,ncd.section)).foreach { cnt =>
      if(cnt>0) {
        db.run(Courses.filter(cr => cr.code === ncd.code && cr.semester === ncd.semester && cr.section === ncd.section).result.head).foreach(cr => {
          // Add current user as instructor
          db.run(UserCourseAssoc += UserCourseAssocRow(Some(userid),Some(cr.courseid),Instructor))
          // Parse instructors and add associations
          val instructors = ncd.instructorNames.split("\\s+").filter(_.trim.nonEmpty)
          instructors.foreach(i => {
            db.run(Users.filter(u => u.username === i).result).foreach { matches => {
              if(matches.isEmpty) println("Attempt to add non-existant instructor: "+i)
              else db.run( UserCourseAssoc += UserCourseAssocRow(Some(matches.head.userid),Some(cr.courseid),Instructor) )
            } }
          })
          // Parse students and add associations
          ncd.studentData.split("\n").foreach(l => println("Line = "+l))
          val d = (for(StudentRegex(id,uname) <- ncd.studentData.split("\n").map(_.trim)) yield {
            println("Matching "+id+", "+uname)
            (id,uname)
          }).dropWhile(_._1 == null)
          d foreach println
          if(d.length>1) {
            val userTuples = for(((a,null),(null,d)) <- d.zip(d.tail)) yield (a,d)
            userTuples foreach println
            for((id,uname) <- userTuples) {
              db.run(Users.filter(u => u.username === uname).result).foreach { matches => {
                if(matches.isEmpty) {
                  db.run( Users += UsersRow(0,uname,id) ).foreach(_ =>
                    db.run(Users.filter(u => u.username === uname).result).foreach { newUser =>
                      db.run( UserCourseAssoc += UserCourseAssocRow(Some(newUser.head.userid),Some(cr.courseid),Student) )
                    })
                } else {
                  db.run( UserCourseAssoc += UserCourseAssocRow(Some(matches.head.userid),Some(cr.courseid),Student) )
                }
              } }
            }
          }
        })
      } else {
        println("cnt was "+cnt+" on insert of course")
      }
    }
  }
  
  def loadCourseData(courseid:Int, db:Database)(implicit ec: ExecutionContext):Future[CourseData] = {
    val courseRow = db.run(Courses.filter(_.courseid === courseid).result.head)
    val instructors = db.run(
      (for {
        (row,assoc) <- Users join UserCourseAssoc on (_.userid === _.userid)
        if assoc.courseid === courseid && assoc.role === Instructor
      } yield row).result
    )
    val quizzes = db.run(
      (for {
        (quiz,assoc) <- Quizzes join QuizCourseCloseAssoc on (_.quizid === _.quizid)
        if assoc.courseid === courseid
      } yield (quiz,assoc)).result
    )
    val totals = quizzes.flatMap(s => Future.sequence(s.map(q => numberOfQuestions(q._1.quizid,db))))
    val students = db.run(
      (for {
        (row,assoc) <- Users join UserCourseAssoc on (_.userid === _.userid)
        if assoc.courseid === courseid && assoc.role === Student
      } yield row).result
    )
    val studentData = students.flatMap(sur => Future.sequence(sur.map(sr => {
      quizzes.flatMap(sq => Future.sequence(sq.map(q => numberOfCorrectQuestions(q._1.quizid, sr.userid, db)))).map(correct =>
        StudentData(sr.userid,sr.username,correct.sum))
    })))
    for {
      cr <- courseRow
      is <- instructors
      t <- totals
      sd <- studentData
      q <- quizzes
    } yield {
      CourseData(cr,is,sd,q.map(t => CourseQuizData(t._1,t._2.closeTime)),t.sum)
    }
  }

  // Users
  def validLogin(username: String, password: String, db: Database)(implicit ec: ExecutionContext): Future[Int] = {
    val matches = db.run(Users.filter(u => u.username === username && u.trinityid === password).result)
    matches.map(us => if (us.isEmpty) -1 else us.head.userid)
  }

  def fetchUserByName(username: String, db: Database)(implicit ec: ExecutionContext): Future[UsersRow] = {
    db.run { Users.filter(u => u.username === username).result.head }
  }
  
  def instructorCourseIds(userid:Int, db:Database)(implicit ec: ExecutionContext): Future[Seq[Option[Int]]] = {
    db.run(UserCourseAssoc.filter(uca => uca.userid === userid && uca.role === Instructor).map(_.courseid).result)
  }

  def instructorCourseRows(userid:Int, db:Database)(implicit ec: ExecutionContext): Future[Seq[CoursesRow]] = {
    db.run {
      (for {
        (ucar,cr) <- UserCourseAssoc join Courses on (_.courseid === _.courseid)
        if ucar.userid === userid && ucar.role === Instructor
      } yield {
        cr
      }).result
    }
  }

  // Quizzes
  def allQuizzesForClass(courseid: Int, db: Database)(implicit ec: ExecutionContext): Future[Seq[(QuizzesRow, Timestamp)]] = {
    db.run {
      (for {
        qca <- QuizCourseCloseAssoc
        if qca.courseid === courseid
        q <- Quizzes
        if q.quizid === qca.quizid
      } yield (q, qca.closeTime)).result
    }
  }

  def currentQuizzesForClass(courseid: Int, db: Database)(implicit ec: ExecutionContext): Future[Seq[QuizzesRow]] = {
    val now = new Timestamp(new Date().getTime)
    db.run {
      (for {
        qca <- QuizCourseCloseAssoc
        if qca.courseid === courseid && qca.closeTime > now
        q <- Quizzes
        if q.quizid === qca.quizid
      } yield q).result
    }
  }

  def numberOfQuestions(quizid: Int, db: Database)(implicit ec: ExecutionContext): Future[Int] = {
    val mcCount = db.run {
      MultipleChoiceAssoc.filter(_.quizid === quizid).length.result
    }
    val funcCount = db.run {
      FunctionAssoc.filter(_.quizid === quizid).length.result
    }
    val lambdaCount = db.run {
      LambdaAssoc.filter(_.quizid === quizid).length.result
    }
    val exprCount = db.run {
      ExpressionAssoc.filter(_.quizid === quizid).length.result
    }
    for(mc <- mcCount; fc <- funcCount; lc <- lambdaCount; ec <- exprCount) yield mc+fc+lc+ec
  }
  
  def numberOfCorrectQuestions(quizid: Int, userid: Int, db: Database)(implicit ec: ExecutionContext): Future[Int] = {
    val mcCount = db.run {
      McAnswers.filter(a => a.quizid === quizid && a.userid === userid && a.correct).map(_.mcQuestionId).result
    }
    val codeCount = db.run {
      CodeAnswers.filter(a => a.quizid === quizid && a.userid === userid && a.correct)
        .map(a => a.questionId -> a.questionType).result
    }
    for{
      mc <- mcCount
      code <- codeCount
    } yield {
      mc.distinct.length + code.distinct.length
    }
  }

  def quizScore(quizid: Int, userid: Int, db: Database)(implicit ec: ExecutionContext): Future[(Int, Int)] = {
    val total = numberOfQuestions(quizid,db)
    val correct = numberOfCorrectQuestions(quizid, userid, db);
    for(t <- total; c <- correct) yield (c,t)
  }
  
  def multipleChoiceData(quizid:Int, userid:Int, db:Database)(implicit ec: ExecutionContext): Future[Seq[MultipleChoiceData]] = {
    val rows = db.run {
      (for {
        (mcq,mca) <- MultipleChoiceQuestions join MultipleChoiceAssoc on (_.mcQuestionId === _.mcQuestionId)
        if mca.quizid === quizid
      } yield mcq).result
    }
    val data = rows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.multipleChoice(row.mcQuestionId, db)
        val userAnswer = db.run((for{
          ans <- McAnswers
          if ans.userid === userid && ans.quizid === quizid && ans.mcQuestionId === row.mcQuestionId
        } yield ans.selection).result)
        for{
          spec <- fspec
          ans <- userAnswer
        } yield MultipleChoiceData(row.mcQuestionId, spec, if(ans.isEmpty) None else Some(ans.head))
      }
    })
    data
  }

  def codeQuestionData(quizid:Int, userid:Int, db:Database)(implicit ec: ExecutionContext): Future[Seq[CodeQuestionData]] = {
    val funcRows = db.run {
      (for {
        (fq,fa) <- FunctionQuestions join FunctionAssoc on (_.funcQuestionId === _.funcQuestionId)
        if fa.quizid === quizid
      } yield fq).result
    }
    val funcData = funcRows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.writeFunction(row.funcQuestionId, db)
        val userAnswers = db.run((for{
          ans <- CodeAnswers
          if ans.userid === userid && ans.quizid === quizid && ans.questionId === row.funcQuestionId && ans.questionType === ProblemSpec.FunctionType
        } yield ans).result)
        for {
          spec <- fspec
          ans <- userAnswers
        } yield CodeQuestionData(row.funcQuestionId, 1, spec, if(ans.isEmpty) None else Some(ans.last.answer), ans.exists(_.correct))
      }
    })
    val lambdaRows = db.run {
      (for {
        (lq,la) <- LambdaQuestions join LambdaAssoc on (_.lambdaQuestionId === _.lambdaQuestionId)
        if la.quizid === quizid
      } yield lq).result
    }
    val lambdaData = lambdaRows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.writeLambda(row.lambdaQuestionId, db)
        val userAnswers = db.run((for{
          ans <- CodeAnswers
          if ans.userid === userid && ans.quizid === quizid && ans.questionId === row.lambdaQuestionId && ans.questionType === ProblemSpec.LambdaType
        } yield ans).result)
        for {
          spec <- fspec
          ans <- userAnswers
        } yield CodeQuestionData(row.lambdaQuestionId, 2, spec, if(ans.isEmpty) None else Some(ans.last.answer), ans.exists(_.correct))
      }
    })
    val exprRows = db.run {
      (for {
        (eq,ea) <- ExpressionQuestions join ExpressionAssoc on (_.exprQuestionId === _.exprQuestionId)
        if ea.quizid === quizid
      } yield eq).result
    }
    val exprData = exprRows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.writeExpression(row.exprQuestionId, db)
        val userAnswers = db.run((for{
          ans <- CodeAnswers
          if ans.userid === userid && ans.quizid === quizid && ans.questionId === row.exprQuestionId && ans.questionType === ProblemSpec.ExpressionType
        } yield ans).result)
        for {
          spec <- fspec
          ans <- userAnswers
        } yield CodeQuestionData(row.exprQuestionId, 3, spec, if(ans.isEmpty) None else Some(ans.last.answer), ans.exists(_.correct))
      }
    })
    for {
      fd <- funcData
      ld <- lambdaData
      ed <- exprData
    } yield fd ++ ld ++ ed
  }

  def quizData(quizid:Int, userid:Int, db:Database)(implicit ec: ExecutionContext): Future[QuizData] = {
    val quizRow = db.run(Quizzes.filter(_.quizid === quizid).result.head)
    val mcQuestions = multipleChoiceData(quizid, userid, db)
    val codeQuestions = codeQuestionData(quizid, userid, db)
    for {
      qr <- quizRow
      mc <- mcQuestions
      cq <- codeQuestions
    } yield QuizData(quizid,userid,qr.name,qr.description,mc,cq)
  }
  
  def quizSpecs(quizid:Int, db:Database)(implicit ec: ExecutionContext): Future[Seq[ProblemSpec]] = {
    val mcQuestions = db.run(MultipleChoiceAssoc.filter(_.quizid === quizid).result).flatMap(f => Future.sequence(f.map(mca => {
      db.run(MultipleChoiceQuestions.filter(_.mcQuestionId === mca.mcQuestionId).result)
    })))
    val mcSpecs = mcQuestions.flatMap(mcqSeq => Future.sequence(mcqSeq.flatten.map(mcq => 
      ProblemSpecRunner(ProblemSpec.MultipleChoiceType,mcq.mcQuestionId,db))))
    val funcQuestions = db.run(FunctionAssoc.filter(_.quizid === quizid).result).flatMap(f => Future.sequence(f.map(fa => {
      db.run(FunctionQuestions.filter(_.funcQuestionId === fa.funcQuestionId).result)
    })))
    val funcSpecs = funcQuestions.flatMap(fqSeq => Future.sequence(fqSeq.flatten.map(fq => 
      ProblemSpecRunner(ProblemSpec.FunctionType,fq.funcQuestionId,db))))
    val lambdaQuestions = db.run(LambdaAssoc.filter(_.quizid === quizid).result).flatMap(f => Future.sequence(f.map(la => {
      db.run(LambdaQuestions.filter(_.lambdaQuestionId === la.lambdaQuestionId).result)
    })))
    val lambdaSpecs = lambdaQuestions.flatMap(lqSeq => Future.sequence(lqSeq.flatten.map(lq => 
      ProblemSpecRunner(ProblemSpec.LambdaType,lq.lambdaQuestionId,db))))
    val exprQuestions = db.run(ExpressionAssoc.filter(_.quizid === quizid).result).flatMap(f => Future.sequence(f.map(ea => {
      db.run(ExpressionQuestions.filter(_.exprQuestionId === ea.exprQuestionId).result)
    })))
    val exprSpecs = exprQuestions.flatMap(eqSeq => Future.sequence(eqSeq.flatten.map(eq => 
      ProblemSpecRunner(ProblemSpec.ExpressionType,eq.exprQuestionId,db))))
    for {
        mcqs <- mcSpecs
        fqs <- funcSpecs
        lqs <- lambdaSpecs
        eqs <- exprSpecs
    } yield { mcqs ++ fqs ++ lqs ++ eqs }
  }
  
  def quizResults(quizid: Int, courseid: Int, db: Database)(implicit ec: ExecutionContext):Future[QuizResultsData] = {
    val quizRow = db.run(Quizzes.filter(_.quizid === quizid).result.head)
    val mcQuestions = multipleChoiceResultsData(quizid, courseid, db)
    val codeQuestions = codeQuestionResultsData(quizid, courseid, db)
    for {
      qr <- quizRow
      mc <- mcQuestions
      cq <- codeQuestions
    } yield QuizResultsData(quizid,courseid,qr.name,qr.description,mc,cq)
  }

  def multipleChoiceResultsData(quizid:Int, courseid:Int, db: Database)(implicit ec: ExecutionContext):Future[Seq[MultipleChoiceResultsData]] = {
    val rows = db.run {
      (for {
        (mcq,mca) <- MultipleChoiceQuestions join MultipleChoiceAssoc on (_.mcQuestionId === _.mcQuestionId)
        if mca.quizid === quizid
      } yield mcq).result
    }
    val data = rows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.multipleChoice(row.mcQuestionId, db)
        val courseAnswer = db.run((for{
          uca <- UserCourseAssoc
          if uca.courseid === courseid
          ans <- McAnswers
          if ans.userid === uca.userid && ans.quizid === quizid && ans.mcQuestionId === row.mcQuestionId
        } yield ans.selection).result)
        for{
          spec <- fspec
          ans <- courseAnswer
        } yield {
          val ansCounts = Array.fill(spec.options.length)(0)
          for(a <- ans) ansCounts(a-1) += 1
          MultipleChoiceResultsData(spec, ansCounts)
        }
      }
    })
    data
  }
  
  def codeQuestionResultsData(quizid:Int, courseid:Int, db: Database)(implicit ec: ExecutionContext):Future[Seq[CodeQuestionResultsData]] = {
    val funcRows = db.run {
      (for {
        (fq,fa) <- FunctionQuestions join FunctionAssoc on (_.funcQuestionId === _.funcQuestionId)
        if fa.quizid === quizid
      } yield fq).result
    }
    val funcData = funcRows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.writeFunction(row.funcQuestionId, db)
        val courseAnswers = db.run((for{
          uca <- UserCourseAssoc
          if uca.courseid === courseid
          ans <- CodeAnswers
          if ans.userid === uca.userid && ans.quizid === quizid && ans.questionId === row.funcQuestionId && ans.questionType === ProblemSpec.FunctionType
        } yield ans).result)
        for {
          spec <- fspec
          ans <- courseAnswers
        } yield CodeQuestionResultsData(spec.prompt, ans.length, ans.map(_.userid).distinct.length, ans.count(_.correct))
      }
    })
    val lambdaRows = db.run {
      (for {
        (lq,la) <- LambdaQuestions join LambdaAssoc on (_.lambdaQuestionId === _.lambdaQuestionId)
        if la.quizid === quizid
      } yield lq).result
    }
    val lambdaData = lambdaRows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.writeLambda(row.lambdaQuestionId, db)
        val courseAnswers = db.run((for{
          uca <- UserCourseAssoc
          if uca.courseid === courseid
          ans <- CodeAnswers
          if ans.userid === uca.userid && ans.quizid === quizid && ans.questionId === row.lambdaQuestionId && ans.questionType === ProblemSpec.LambdaType
        } yield ans).result)
        for {
          spec <- fspec
          ans <- courseAnswers
        } yield CodeQuestionResultsData(spec.prompt, ans.length, ans.map(_.userid).distinct.length, ans.count(_.correct))
      }
    })
    val exprRows = db.run {
      (for {
        (eq,ea) <- ExpressionQuestions join ExpressionAssoc on (_.exprQuestionId === _.exprQuestionId)
        if ea.quizid === quizid
      } yield eq).result
    }
    val exprData = exprRows.flatMap(s => Future.sequence { 
      for {
        row <- s
      } yield {
        val fspec = ProblemSpecRunner.writeExpression(row.exprQuestionId, db)
        val userAnswers = db.run((for{
          uca <- UserCourseAssoc
          if uca.courseid === courseid
          ans <- CodeAnswers
          if ans.userid === uca.userid && ans.quizid === quizid && ans.questionId === row.exprQuestionId && ans.questionType === ProblemSpec.ExpressionType
        } yield ans).result)
        for {
          spec <- fspec
          ans <- userAnswers
        } yield CodeQuestionResultsData(spec.prompt, ans.length, ans.map(_.userid).distinct.length, ans.count(_.correct))
      }
    })
    for {
      fd <- funcData
      ld <- lambdaData
      ed <- exprData
    } yield fd ++ ld ++ ed
  }
}