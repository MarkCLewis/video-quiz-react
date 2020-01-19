package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(CodeAnswers.schema, Courses.schema, ExpressionAssoc.schema, ExpressionQuestions.schema, FunctionAssoc.schema, FunctionQuestions.schema, LambdaAssoc.schema, LambdaQuestions.schema, McAnswers.schema, MultipleChoiceAssoc.schema, MultipleChoiceQuestions.schema, QuizCourseCloseAssoc.schema, Quizzes.schema, UserCourseAssoc.schema, Users.schema, VariableSpecifications.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table CodeAnswers
   *  @param userid Database column userid SqlType(INT), Default(None)
   *  @param quizid Database column quizid SqlType(INT), Default(None)
   *  @param questionId Database column question_id SqlType(INT)
   *  @param questionType Database column question_type SqlType(INT)
   *  @param answer Database column answer SqlType(VARCHAR), Length(4000,true)
   *  @param correct Database column correct SqlType(BIT)
   *  @param answerTime Database column answer_time SqlType(TIMESTAMP) */
  case class CodeAnswersRow(userid: Option[Int] = None, quizid: Option[Int] = None, questionId: Int, questionType: Int, answer: String, correct: Boolean, answerTime: java.sql.Timestamp)
  /** GetResult implicit for fetching CodeAnswersRow objects using plain SQL queries */
  implicit def GetResultCodeAnswersRow(implicit e0: GR[Option[Int]], e1: GR[Int], e2: GR[String], e3: GR[Boolean], e4: GR[java.sql.Timestamp]): GR[CodeAnswersRow] = GR{
    prs => import prs._
    CodeAnswersRow.tupled((<<?[Int], <<?[Int], <<[Int], <<[Int], <<[String], <<[Boolean], <<[java.sql.Timestamp]))
  }
  /** Table description of table code_answers. Objects of this class serve as prototypes for rows in queries. */
  class CodeAnswers(_tableTag: Tag) extends profile.api.Table[CodeAnswersRow](_tableTag, Some("video_quizzes"), "code_answers") {
    def * = (userid, quizid, questionId, questionType, answer, correct, answerTime) <> (CodeAnswersRow.tupled, CodeAnswersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (userid, quizid, Rep.Some(questionId), Rep.Some(questionType), Rep.Some(answer), Rep.Some(correct), Rep.Some(answerTime)).shaped.<>({r=>import r._; _3.map(_=> CodeAnswersRow.tupled((_1, _2, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column userid SqlType(INT), Default(None) */
    val userid: Rep[Option[Int]] = column[Option[Int]]("userid", O.Default(None))
    /** Database column quizid SqlType(INT), Default(None) */
    val quizid: Rep[Option[Int]] = column[Option[Int]]("quizid", O.Default(None))
    /** Database column question_id SqlType(INT) */
    val questionId: Rep[Int] = column[Int]("question_id")
    /** Database column question_type SqlType(INT) */
    val questionType: Rep[Int] = column[Int]("question_type")
    /** Database column answer SqlType(VARCHAR), Length(4000,true) */
    val answer: Rep[String] = column[String]("answer", O.Length(4000,varying=true))
    /** Database column correct SqlType(BIT) */
    val correct: Rep[Boolean] = column[Boolean]("correct")
    /** Database column answer_time SqlType(TIMESTAMP) */
    val answerTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("answer_time")

    /** Foreign key referencing Quizzes (database name code_answers_ibfk_2) */
    lazy val quizzesFk = foreignKey("code_answers_ibfk_2", quizid, Quizzes)(r => Rep.Some(r.quizid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name code_answers_ibfk_1) */
    lazy val usersFk = foreignKey("code_answers_ibfk_1", userid, Users)(r => Rep.Some(r.userid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table CodeAnswers */
  lazy val CodeAnswers = new TableQuery(tag => new CodeAnswers(tag))

  /** Entity class storing rows of table Courses
   *  @param courseid Database column courseid SqlType(INT), AutoInc, PrimaryKey
   *  @param code Database column code SqlType(VARCHAR), Length(8,true)
   *  @param semester Database column semester SqlType(VARCHAR), Length(3,true)
   *  @param section Database column section SqlType(INT) */
  case class CoursesRow(courseid: Int, code: String, semester: String, section: Int)
  /** GetResult implicit for fetching CoursesRow objects using plain SQL queries */
  implicit def GetResultCoursesRow(implicit e0: GR[Int], e1: GR[String]): GR[CoursesRow] = GR{
    prs => import prs._
    CoursesRow.tupled((<<[Int], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table courses. Objects of this class serve as prototypes for rows in queries. */
  class Courses(_tableTag: Tag) extends profile.api.Table[CoursesRow](_tableTag, Some("video_quizzes"), "courses") {
    def * = (courseid, code, semester, section) <> (CoursesRow.tupled, CoursesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(courseid), Rep.Some(code), Rep.Some(semester), Rep.Some(section)).shaped.<>({r=>import r._; _1.map(_=> CoursesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column courseid SqlType(INT), AutoInc, PrimaryKey */
    val courseid: Rep[Int] = column[Int]("courseid", O.AutoInc, O.PrimaryKey)
    /** Database column code SqlType(VARCHAR), Length(8,true) */
    val code: Rep[String] = column[String]("code", O.Length(8,varying=true))
    /** Database column semester SqlType(VARCHAR), Length(3,true) */
    val semester: Rep[String] = column[String]("semester", O.Length(3,varying=true))
    /** Database column section SqlType(INT) */
    val section: Rep[Int] = column[Int]("section")
  }
  /** Collection-like TableQuery object for table Courses */
  lazy val Courses = new TableQuery(tag => new Courses(tag))

  /** Entity class storing rows of table ExpressionAssoc
   *  @param quizid Database column quizid SqlType(INT), Default(None)
   *  @param exprQuestionId Database column expr_question_id SqlType(INT), Default(None) */
  case class ExpressionAssocRow(quizid: Option[Int] = None, exprQuestionId: Option[Int] = None)
  /** GetResult implicit for fetching ExpressionAssocRow objects using plain SQL queries */
  implicit def GetResultExpressionAssocRow(implicit e0: GR[Option[Int]]): GR[ExpressionAssocRow] = GR{
    prs => import prs._
    ExpressionAssocRow.tupled((<<?[Int], <<?[Int]))
  }
  /** Table description of table expression_assoc. Objects of this class serve as prototypes for rows in queries. */
  class ExpressionAssoc(_tableTag: Tag) extends profile.api.Table[ExpressionAssocRow](_tableTag, Some("video_quizzes"), "expression_assoc") {
    def * = (quizid, exprQuestionId) <> (ExpressionAssocRow.tupled, ExpressionAssocRow.unapply)

    /** Database column quizid SqlType(INT), Default(None) */
    val quizid: Rep[Option[Int]] = column[Option[Int]]("quizid", O.Default(None))
    /** Database column expr_question_id SqlType(INT), Default(None) */
    val exprQuestionId: Rep[Option[Int]] = column[Option[Int]]("expr_question_id", O.Default(None))

    /** Foreign key referencing ExpressionQuestions (database name expression_assoc_ibfk_2) */
    lazy val expressionQuestionsFk = foreignKey("expression_assoc_ibfk_2", exprQuestionId, ExpressionQuestions)(r => Rep.Some(r.exprQuestionId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Quizzes (database name expression_assoc_ibfk_1) */
    lazy val quizzesFk = foreignKey("expression_assoc_ibfk_1", quizid, Quizzes)(r => Rep.Some(r.quizid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table ExpressionAssoc */
  lazy val ExpressionAssoc = new TableQuery(tag => new ExpressionAssoc(tag))

  /** Entity class storing rows of table ExpressionQuestions
   *  @param exprQuestionId Database column expr_question_id SqlType(INT), AutoInc, PrimaryKey
   *  @param prompt Database column prompt SqlType(VARCHAR), Length(4000,true)
   *  @param correctCode Database column correct_code SqlType(VARCHAR), Length(4000,true)
   *  @param generalSetup Database column general_setup SqlType(VARCHAR), Length(4000,true)
   *  @param numRuns Database column num_runs SqlType(INT) */
  case class ExpressionQuestionsRow(exprQuestionId: Int, prompt: String, correctCode: String, generalSetup: String, numRuns: Int)
  /** GetResult implicit for fetching ExpressionQuestionsRow objects using plain SQL queries */
  implicit def GetResultExpressionQuestionsRow(implicit e0: GR[Int], e1: GR[String]): GR[ExpressionQuestionsRow] = GR{
    prs => import prs._
    ExpressionQuestionsRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table expression_questions. Objects of this class serve as prototypes for rows in queries. */
  class ExpressionQuestions(_tableTag: Tag) extends profile.api.Table[ExpressionQuestionsRow](_tableTag, Some("video_quizzes"), "expression_questions") {
    def * = (exprQuestionId, prompt, correctCode, generalSetup, numRuns) <> (ExpressionQuestionsRow.tupled, ExpressionQuestionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(exprQuestionId), Rep.Some(prompt), Rep.Some(correctCode), Rep.Some(generalSetup), Rep.Some(numRuns)).shaped.<>({r=>import r._; _1.map(_=> ExpressionQuestionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column expr_question_id SqlType(INT), AutoInc, PrimaryKey */
    val exprQuestionId: Rep[Int] = column[Int]("expr_question_id", O.AutoInc, O.PrimaryKey)
    /** Database column prompt SqlType(VARCHAR), Length(4000,true) */
    val prompt: Rep[String] = column[String]("prompt", O.Length(4000,varying=true))
    /** Database column correct_code SqlType(VARCHAR), Length(4000,true) */
    val correctCode: Rep[String] = column[String]("correct_code", O.Length(4000,varying=true))
    /** Database column general_setup SqlType(VARCHAR), Length(4000,true) */
    val generalSetup: Rep[String] = column[String]("general_setup", O.Length(4000,varying=true))
    /** Database column num_runs SqlType(INT) */
    val numRuns: Rep[Int] = column[Int]("num_runs")
  }
  /** Collection-like TableQuery object for table ExpressionQuestions */
  lazy val ExpressionQuestions = new TableQuery(tag => new ExpressionQuestions(tag))

  /** Entity class storing rows of table FunctionAssoc
   *  @param quizid Database column quizid SqlType(INT), Default(None)
   *  @param funcQuestionId Database column func_question_id SqlType(INT), Default(None) */
  case class FunctionAssocRow(quizid: Option[Int] = None, funcQuestionId: Option[Int] = None)
  /** GetResult implicit for fetching FunctionAssocRow objects using plain SQL queries */
  implicit def GetResultFunctionAssocRow(implicit e0: GR[Option[Int]]): GR[FunctionAssocRow] = GR{
    prs => import prs._
    FunctionAssocRow.tupled((<<?[Int], <<?[Int]))
  }
  /** Table description of table function_assoc. Objects of this class serve as prototypes for rows in queries. */
  class FunctionAssoc(_tableTag: Tag) extends profile.api.Table[FunctionAssocRow](_tableTag, Some("video_quizzes"), "function_assoc") {
    def * = (quizid, funcQuestionId) <> (FunctionAssocRow.tupled, FunctionAssocRow.unapply)

    /** Database column quizid SqlType(INT), Default(None) */
    val quizid: Rep[Option[Int]] = column[Option[Int]]("quizid", O.Default(None))
    /** Database column func_question_id SqlType(INT), Default(None) */
    val funcQuestionId: Rep[Option[Int]] = column[Option[Int]]("func_question_id", O.Default(None))

    /** Foreign key referencing FunctionQuestions (database name function_assoc_ibfk_2) */
    lazy val functionQuestionsFk = foreignKey("function_assoc_ibfk_2", funcQuestionId, FunctionQuestions)(r => Rep.Some(r.funcQuestionId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Quizzes (database name function_assoc_ibfk_1) */
    lazy val quizzesFk = foreignKey("function_assoc_ibfk_1", quizid, Quizzes)(r => Rep.Some(r.quizid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table FunctionAssoc */
  lazy val FunctionAssoc = new TableQuery(tag => new FunctionAssoc(tag))

  /** Entity class storing rows of table FunctionQuestions
   *  @param funcQuestionId Database column func_question_id SqlType(INT), AutoInc, PrimaryKey
   *  @param prompt Database column prompt SqlType(VARCHAR), Length(4000,true)
   *  @param correctCode Database column correct_code SqlType(VARCHAR), Length(4000,true)
   *  @param functionName Database column function_name SqlType(VARCHAR), Length(40,true)
   *  @param numRuns Database column num_runs SqlType(INT) */
  case class FunctionQuestionsRow(funcQuestionId: Int, prompt: String, correctCode: String, functionName: String, numRuns: Int)
  /** GetResult implicit for fetching FunctionQuestionsRow objects using plain SQL queries */
  implicit def GetResultFunctionQuestionsRow(implicit e0: GR[Int], e1: GR[String]): GR[FunctionQuestionsRow] = GR{
    prs => import prs._
    FunctionQuestionsRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table function_questions. Objects of this class serve as prototypes for rows in queries. */
  class FunctionQuestions(_tableTag: Tag) extends profile.api.Table[FunctionQuestionsRow](_tableTag, Some("video_quizzes"), "function_questions") {
    def * = (funcQuestionId, prompt, correctCode, functionName, numRuns) <> (FunctionQuestionsRow.tupled, FunctionQuestionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(funcQuestionId), Rep.Some(prompt), Rep.Some(correctCode), Rep.Some(functionName), Rep.Some(numRuns)).shaped.<>({r=>import r._; _1.map(_=> FunctionQuestionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column func_question_id SqlType(INT), AutoInc, PrimaryKey */
    val funcQuestionId: Rep[Int] = column[Int]("func_question_id", O.AutoInc, O.PrimaryKey)
    /** Database column prompt SqlType(VARCHAR), Length(4000,true) */
    val prompt: Rep[String] = column[String]("prompt", O.Length(4000,varying=true))
    /** Database column correct_code SqlType(VARCHAR), Length(4000,true) */
    val correctCode: Rep[String] = column[String]("correct_code", O.Length(4000,varying=true))
    /** Database column function_name SqlType(VARCHAR), Length(40,true) */
    val functionName: Rep[String] = column[String]("function_name", O.Length(40,varying=true))
    /** Database column num_runs SqlType(INT) */
    val numRuns: Rep[Int] = column[Int]("num_runs")
  }
  /** Collection-like TableQuery object for table FunctionQuestions */
  lazy val FunctionQuestions = new TableQuery(tag => new FunctionQuestions(tag))

  /** Entity class storing rows of table LambdaAssoc
   *  @param quizid Database column quizid SqlType(INT), Default(None)
   *  @param lambdaQuestionId Database column lambda_question_id SqlType(INT), Default(None) */
  case class LambdaAssocRow(quizid: Option[Int] = None, lambdaQuestionId: Option[Int] = None)
  /** GetResult implicit for fetching LambdaAssocRow objects using plain SQL queries */
  implicit def GetResultLambdaAssocRow(implicit e0: GR[Option[Int]]): GR[LambdaAssocRow] = GR{
    prs => import prs._
    LambdaAssocRow.tupled((<<?[Int], <<?[Int]))
  }
  /** Table description of table lambda_assoc. Objects of this class serve as prototypes for rows in queries. */
  class LambdaAssoc(_tableTag: Tag) extends profile.api.Table[LambdaAssocRow](_tableTag, Some("video_quizzes"), "lambda_assoc") {
    def * = (quizid, lambdaQuestionId) <> (LambdaAssocRow.tupled, LambdaAssocRow.unapply)

    /** Database column quizid SqlType(INT), Default(None) */
    val quizid: Rep[Option[Int]] = column[Option[Int]]("quizid", O.Default(None))
    /** Database column lambda_question_id SqlType(INT), Default(None) */
    val lambdaQuestionId: Rep[Option[Int]] = column[Option[Int]]("lambda_question_id", O.Default(None))

    /** Foreign key referencing LambdaQuestions (database name lambda_assoc_ibfk_2) */
    lazy val lambdaQuestionsFk = foreignKey("lambda_assoc_ibfk_2", lambdaQuestionId, LambdaQuestions)(r => Rep.Some(r.lambdaQuestionId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Quizzes (database name lambda_assoc_ibfk_1) */
    lazy val quizzesFk = foreignKey("lambda_assoc_ibfk_1", quizid, Quizzes)(r => Rep.Some(r.quizid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table LambdaAssoc */
  lazy val LambdaAssoc = new TableQuery(tag => new LambdaAssoc(tag))

  /** Entity class storing rows of table LambdaQuestions
   *  @param lambdaQuestionId Database column lambda_question_id SqlType(INT), AutoInc, PrimaryKey
   *  @param prompt Database column prompt SqlType(VARCHAR), Length(4000,true)
   *  @param correctCode Database column correct_code SqlType(VARCHAR), Length(4000,true)
   *  @param returnType Database column return_type SqlType(VARCHAR), Length(40,true)
   *  @param numRuns Database column num_runs SqlType(INT) */
  case class LambdaQuestionsRow(lambdaQuestionId: Int, prompt: String, correctCode: String, returnType: String, numRuns: Int)
  /** GetResult implicit for fetching LambdaQuestionsRow objects using plain SQL queries */
  implicit def GetResultLambdaQuestionsRow(implicit e0: GR[Int], e1: GR[String]): GR[LambdaQuestionsRow] = GR{
    prs => import prs._
    LambdaQuestionsRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table lambda_questions. Objects of this class serve as prototypes for rows in queries. */
  class LambdaQuestions(_tableTag: Tag) extends profile.api.Table[LambdaQuestionsRow](_tableTag, Some("video_quizzes"), "lambda_questions") {
    def * = (lambdaQuestionId, prompt, correctCode, returnType, numRuns) <> (LambdaQuestionsRow.tupled, LambdaQuestionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(lambdaQuestionId), Rep.Some(prompt), Rep.Some(correctCode), Rep.Some(returnType), Rep.Some(numRuns)).shaped.<>({r=>import r._; _1.map(_=> LambdaQuestionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column lambda_question_id SqlType(INT), AutoInc, PrimaryKey */
    val lambdaQuestionId: Rep[Int] = column[Int]("lambda_question_id", O.AutoInc, O.PrimaryKey)
    /** Database column prompt SqlType(VARCHAR), Length(4000,true) */
    val prompt: Rep[String] = column[String]("prompt", O.Length(4000,varying=true))
    /** Database column correct_code SqlType(VARCHAR), Length(4000,true) */
    val correctCode: Rep[String] = column[String]("correct_code", O.Length(4000,varying=true))
    /** Database column return_type SqlType(VARCHAR), Length(40,true) */
    val returnType: Rep[String] = column[String]("return_type", O.Length(40,varying=true))
    /** Database column num_runs SqlType(INT) */
    val numRuns: Rep[Int] = column[Int]("num_runs")
  }
  /** Collection-like TableQuery object for table LambdaQuestions */
  lazy val LambdaQuestions = new TableQuery(tag => new LambdaQuestions(tag))

  /** Entity class storing rows of table McAnswers
   *  @param userid Database column userid SqlType(INT), Default(None)
   *  @param quizid Database column quizid SqlType(INT), Default(None)
   *  @param mcQuestionId Database column mc_question_id SqlType(INT), Default(None)
   *  @param selection Database column selection SqlType(INT)
   *  @param correct Database column correct SqlType(BIT)
   *  @param answerTime Database column answer_time SqlType(TIMESTAMP) */
  case class McAnswersRow(userid: Option[Int] = None, quizid: Option[Int] = None, mcQuestionId: Option[Int] = None, selection: Int, correct: Boolean, answerTime: java.sql.Timestamp)
  /** GetResult implicit for fetching McAnswersRow objects using plain SQL queries */
  implicit def GetResultMcAnswersRow(implicit e0: GR[Option[Int]], e1: GR[Int], e2: GR[Boolean], e3: GR[java.sql.Timestamp]): GR[McAnswersRow] = GR{
    prs => import prs._
    McAnswersRow.tupled((<<?[Int], <<?[Int], <<?[Int], <<[Int], <<[Boolean], <<[java.sql.Timestamp]))
  }
  /** Table description of table mc_answers. Objects of this class serve as prototypes for rows in queries. */
  class McAnswers(_tableTag: Tag) extends profile.api.Table[McAnswersRow](_tableTag, Some("video_quizzes"), "mc_answers") {
    def * = (userid, quizid, mcQuestionId, selection, correct, answerTime) <> (McAnswersRow.tupled, McAnswersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (userid, quizid, mcQuestionId, Rep.Some(selection), Rep.Some(correct), Rep.Some(answerTime)).shaped.<>({r=>import r._; _4.map(_=> McAnswersRow.tupled((_1, _2, _3, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column userid SqlType(INT), Default(None) */
    val userid: Rep[Option[Int]] = column[Option[Int]]("userid", O.Default(None))
    /** Database column quizid SqlType(INT), Default(None) */
    val quizid: Rep[Option[Int]] = column[Option[Int]]("quizid", O.Default(None))
    /** Database column mc_question_id SqlType(INT), Default(None) */
    val mcQuestionId: Rep[Option[Int]] = column[Option[Int]]("mc_question_id", O.Default(None))
    /** Database column selection SqlType(INT) */
    val selection: Rep[Int] = column[Int]("selection")
    /** Database column correct SqlType(BIT) */
    val correct: Rep[Boolean] = column[Boolean]("correct")
    /** Database column answer_time SqlType(TIMESTAMP) */
    val answerTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("answer_time")

    /** Foreign key referencing MultipleChoiceQuestions (database name mc_answers_ibfk_3) */
    lazy val multipleChoiceQuestionsFk = foreignKey("mc_answers_ibfk_3", mcQuestionId, MultipleChoiceQuestions)(r => Rep.Some(r.mcQuestionId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Quizzes (database name mc_answers_ibfk_2) */
    lazy val quizzesFk = foreignKey("mc_answers_ibfk_2", quizid, Quizzes)(r => Rep.Some(r.quizid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name mc_answers_ibfk_1) */
    lazy val usersFk = foreignKey("mc_answers_ibfk_1", userid, Users)(r => Rep.Some(r.userid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table McAnswers */
  lazy val McAnswers = new TableQuery(tag => new McAnswers(tag))

  /** Entity class storing rows of table MultipleChoiceAssoc
   *  @param quizid Database column quizid SqlType(INT), Default(None)
   *  @param mcQuestionId Database column mc_question_id SqlType(INT), Default(None) */
  case class MultipleChoiceAssocRow(quizid: Option[Int] = None, mcQuestionId: Option[Int] = None)
  /** GetResult implicit for fetching MultipleChoiceAssocRow objects using plain SQL queries */
  implicit def GetResultMultipleChoiceAssocRow(implicit e0: GR[Option[Int]]): GR[MultipleChoiceAssocRow] = GR{
    prs => import prs._
    MultipleChoiceAssocRow.tupled((<<?[Int], <<?[Int]))
  }
  /** Table description of table multiple_choice_assoc. Objects of this class serve as prototypes for rows in queries. */
  class MultipleChoiceAssoc(_tableTag: Tag) extends profile.api.Table[MultipleChoiceAssocRow](_tableTag, Some("video_quizzes"), "multiple_choice_assoc") {
    def * = (quizid, mcQuestionId) <> (MultipleChoiceAssocRow.tupled, MultipleChoiceAssocRow.unapply)

    /** Database column quizid SqlType(INT), Default(None) */
    val quizid: Rep[Option[Int]] = column[Option[Int]]("quizid", O.Default(None))
    /** Database column mc_question_id SqlType(INT), Default(None) */
    val mcQuestionId: Rep[Option[Int]] = column[Option[Int]]("mc_question_id", O.Default(None))

    /** Foreign key referencing MultipleChoiceQuestions (database name multiple_choice_assoc_ibfk_2) */
    lazy val multipleChoiceQuestionsFk = foreignKey("multiple_choice_assoc_ibfk_2", mcQuestionId, MultipleChoiceQuestions)(r => Rep.Some(r.mcQuestionId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Quizzes (database name multiple_choice_assoc_ibfk_1) */
    lazy val quizzesFk = foreignKey("multiple_choice_assoc_ibfk_1", quizid, Quizzes)(r => Rep.Some(r.quizid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table MultipleChoiceAssoc */
  lazy val MultipleChoiceAssoc = new TableQuery(tag => new MultipleChoiceAssoc(tag))

  /** Entity class storing rows of table MultipleChoiceQuestions
   *  @param mcQuestionId Database column mc_question_id SqlType(INT), AutoInc, PrimaryKey
   *  @param prompt Database column prompt SqlType(VARCHAR), Length(4000,true)
   *  @param option1 Database column option1 SqlType(VARCHAR), Length(4000,true)
   *  @param option2 Database column option2 SqlType(VARCHAR), Length(4000,true)
   *  @param option3 Database column option3 SqlType(VARCHAR), Length(4000,true), Default(None)
   *  @param option4 Database column option4 SqlType(VARCHAR), Length(4000,true), Default(None)
   *  @param option5 Database column option5 SqlType(VARCHAR), Length(4000,true), Default(None)
   *  @param option6 Database column option6 SqlType(VARCHAR), Length(4000,true), Default(None)
   *  @param option7 Database column option7 SqlType(VARCHAR), Length(4000,true), Default(None)
   *  @param option8 Database column option8 SqlType(VARCHAR), Length(4000,true), Default(None)
   *  @param correctOption Database column correct_option SqlType(INT) */
  case class MultipleChoiceQuestionsRow(mcQuestionId: Int, prompt: String, option1: String, option2: String, option3: Option[String] = None, option4: Option[String] = None, option5: Option[String] = None, option6: Option[String] = None, option7: Option[String] = None, option8: Option[String] = None, correctOption: Int)
  /** GetResult implicit for fetching MultipleChoiceQuestionsRow objects using plain SQL queries */
  implicit def GetResultMultipleChoiceQuestionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[MultipleChoiceQuestionsRow] = GR{
    prs => import prs._
    MultipleChoiceQuestionsRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<[Int]))
  }
  /** Table description of table multiple_choice_questions. Objects of this class serve as prototypes for rows in queries. */
  class MultipleChoiceQuestions(_tableTag: Tag) extends profile.api.Table[MultipleChoiceQuestionsRow](_tableTag, Some("video_quizzes"), "multiple_choice_questions") {
    def * = (mcQuestionId, prompt, option1, option2, option3, option4, option5, option6, option7, option8, correctOption) <> (MultipleChoiceQuestionsRow.tupled, MultipleChoiceQuestionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(mcQuestionId), Rep.Some(prompt), Rep.Some(option1), Rep.Some(option2), option3, option4, option5, option6, option7, option8, Rep.Some(correctOption)).shaped.<>({r=>import r._; _1.map(_=> MultipleChoiceQuestionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column mc_question_id SqlType(INT), AutoInc, PrimaryKey */
    val mcQuestionId: Rep[Int] = column[Int]("mc_question_id", O.AutoInc, O.PrimaryKey)
    /** Database column prompt SqlType(VARCHAR), Length(4000,true) */
    val prompt: Rep[String] = column[String]("prompt", O.Length(4000,varying=true))
    /** Database column option1 SqlType(VARCHAR), Length(4000,true) */
    val option1: Rep[String] = column[String]("option1", O.Length(4000,varying=true))
    /** Database column option2 SqlType(VARCHAR), Length(4000,true) */
    val option2: Rep[String] = column[String]("option2", O.Length(4000,varying=true))
    /** Database column option3 SqlType(VARCHAR), Length(4000,true), Default(None) */
    val option3: Rep[Option[String]] = column[Option[String]]("option3", O.Length(4000,varying=true), O.Default(None))
    /** Database column option4 SqlType(VARCHAR), Length(4000,true), Default(None) */
    val option4: Rep[Option[String]] = column[Option[String]]("option4", O.Length(4000,varying=true), O.Default(None))
    /** Database column option5 SqlType(VARCHAR), Length(4000,true), Default(None) */
    val option5: Rep[Option[String]] = column[Option[String]]("option5", O.Length(4000,varying=true), O.Default(None))
    /** Database column option6 SqlType(VARCHAR), Length(4000,true), Default(None) */
    val option6: Rep[Option[String]] = column[Option[String]]("option6", O.Length(4000,varying=true), O.Default(None))
    /** Database column option7 SqlType(VARCHAR), Length(4000,true), Default(None) */
    val option7: Rep[Option[String]] = column[Option[String]]("option7", O.Length(4000,varying=true), O.Default(None))
    /** Database column option8 SqlType(VARCHAR), Length(4000,true), Default(None) */
    val option8: Rep[Option[String]] = column[Option[String]]("option8", O.Length(4000,varying=true), O.Default(None))
    /** Database column correct_option SqlType(INT) */
    val correctOption: Rep[Int] = column[Int]("correct_option")
  }
  /** Collection-like TableQuery object for table MultipleChoiceQuestions */
  lazy val MultipleChoiceQuestions = new TableQuery(tag => new MultipleChoiceQuestions(tag))

  /** Entity class storing rows of table QuizCourseCloseAssoc
   *  @param quizid Database column quizid SqlType(INT), Default(None)
   *  @param courseid Database column courseid SqlType(INT), Default(None)
   *  @param closeTime Database column close_time SqlType(TIMESTAMP) */
  case class QuizCourseCloseAssocRow(quizid: Option[Int] = None, courseid: Option[Int] = None, closeTime: java.sql.Timestamp)
  /** GetResult implicit for fetching QuizCourseCloseAssocRow objects using plain SQL queries */
  implicit def GetResultQuizCourseCloseAssocRow(implicit e0: GR[Option[Int]], e1: GR[java.sql.Timestamp]): GR[QuizCourseCloseAssocRow] = GR{
    prs => import prs._
    QuizCourseCloseAssocRow.tupled((<<?[Int], <<?[Int], <<[java.sql.Timestamp]))
  }
  /** Table description of table quiz_course_close_assoc. Objects of this class serve as prototypes for rows in queries. */
  class QuizCourseCloseAssoc(_tableTag: Tag) extends profile.api.Table[QuizCourseCloseAssocRow](_tableTag, Some("video_quizzes"), "quiz_course_close_assoc") {
    def * = (quizid, courseid, closeTime) <> (QuizCourseCloseAssocRow.tupled, QuizCourseCloseAssocRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (quizid, courseid, Rep.Some(closeTime)).shaped.<>({r=>import r._; _3.map(_=> QuizCourseCloseAssocRow.tupled((_1, _2, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column quizid SqlType(INT), Default(None) */
    val quizid: Rep[Option[Int]] = column[Option[Int]]("quizid", O.Default(None))
    /** Database column courseid SqlType(INT), Default(None) */
    val courseid: Rep[Option[Int]] = column[Option[Int]]("courseid", O.Default(None))
    /** Database column close_time SqlType(TIMESTAMP) */
    val closeTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("close_time")

    /** Foreign key referencing Courses (database name quiz_course_close_assoc_ibfk_2) */
    lazy val coursesFk = foreignKey("quiz_course_close_assoc_ibfk_2", courseid, Courses)(r => Rep.Some(r.courseid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Quizzes (database name quiz_course_close_assoc_ibfk_1) */
    lazy val quizzesFk = foreignKey("quiz_course_close_assoc_ibfk_1", quizid, Quizzes)(r => Rep.Some(r.quizid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table QuizCourseCloseAssoc */
  lazy val QuizCourseCloseAssoc = new TableQuery(tag => new QuizCourseCloseAssoc(tag))

  /** Entity class storing rows of table Quizzes
   *  @param quizid Database column quizid SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(80,true)
   *  @param description Database column description SqlType(VARCHAR), Length(4000,true) */
  case class QuizzesRow(quizid: Int, name: String, description: String)
  /** GetResult implicit for fetching QuizzesRow objects using plain SQL queries */
  implicit def GetResultQuizzesRow(implicit e0: GR[Int], e1: GR[String]): GR[QuizzesRow] = GR{
    prs => import prs._
    QuizzesRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table quizzes. Objects of this class serve as prototypes for rows in queries. */
  class Quizzes(_tableTag: Tag) extends profile.api.Table[QuizzesRow](_tableTag, Some("video_quizzes"), "quizzes") {
    def * = (quizid, name, description) <> (QuizzesRow.tupled, QuizzesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(quizid), Rep.Some(name), Rep.Some(description)).shaped.<>({r=>import r._; _1.map(_=> QuizzesRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column quizid SqlType(INT), AutoInc, PrimaryKey */
    val quizid: Rep[Int] = column[Int]("quizid", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(80,true) */
    val name: Rep[String] = column[String]("name", O.Length(80,varying=true))
    /** Database column description SqlType(VARCHAR), Length(4000,true) */
    val description: Rep[String] = column[String]("description", O.Length(4000,varying=true))
  }
  /** Collection-like TableQuery object for table Quizzes */
  lazy val Quizzes = new TableQuery(tag => new Quizzes(tag))

  /** Entity class storing rows of table UserCourseAssoc
   *  @param userid Database column userid SqlType(INT), Default(None)
   *  @param courseid Database column courseid SqlType(INT), Default(None)
   *  @param role Database column role SqlType(INT) */
  case class UserCourseAssocRow(userid: Option[Int] = None, courseid: Option[Int] = None, role: Int)
  /** GetResult implicit for fetching UserCourseAssocRow objects using plain SQL queries */
  implicit def GetResultUserCourseAssocRow(implicit e0: GR[Option[Int]], e1: GR[Int]): GR[UserCourseAssocRow] = GR{
    prs => import prs._
    UserCourseAssocRow.tupled((<<?[Int], <<?[Int], <<[Int]))
  }
  /** Table description of table user_course_assoc. Objects of this class serve as prototypes for rows in queries. */
  class UserCourseAssoc(_tableTag: Tag) extends profile.api.Table[UserCourseAssocRow](_tableTag, Some("video_quizzes"), "user_course_assoc") {
    def * = (userid, courseid, role) <> (UserCourseAssocRow.tupled, UserCourseAssocRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (userid, courseid, Rep.Some(role)).shaped.<>({r=>import r._; _3.map(_=> UserCourseAssocRow.tupled((_1, _2, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column userid SqlType(INT), Default(None) */
    val userid: Rep[Option[Int]] = column[Option[Int]]("userid", O.Default(None))
    /** Database column courseid SqlType(INT), Default(None) */
    val courseid: Rep[Option[Int]] = column[Option[Int]]("courseid", O.Default(None))
    /** Database column role SqlType(INT) */
    val role: Rep[Int] = column[Int]("role")

    /** Foreign key referencing Courses (database name user_course_assoc_ibfk_2) */
    lazy val coursesFk = foreignKey("user_course_assoc_ibfk_2", courseid, Courses)(r => Rep.Some(r.courseid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name user_course_assoc_ibfk_1) */
    lazy val usersFk = foreignKey("user_course_assoc_ibfk_1", userid, Users)(r => Rep.Some(r.userid), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table UserCourseAssoc */
  lazy val UserCourseAssoc = new TableQuery(tag => new UserCourseAssoc(tag))

  /** Entity class storing rows of table Users
   *  @param userid Database column userid SqlType(INT), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(VARCHAR), Length(8,true)
   *  @param trinityid Database column trinityid SqlType(VARCHAR), Length(7,true) */
  case class UsersRow(userid: Int, username: String, trinityid: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, Some("video_quizzes"), "users") {
    def * = (userid, username, trinityid) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userid), Rep.Some(username), Rep.Some(trinityid)).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column userid SqlType(INT), AutoInc, PrimaryKey */
    val userid: Rep[Int] = column[Int]("userid", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(VARCHAR), Length(8,true) */
    val username: Rep[String] = column[String]("username", O.Length(8,varying=true))
    /** Database column trinityid SqlType(VARCHAR), Length(7,true) */
    val trinityid: Rep[String] = column[String]("trinityid", O.Length(7,varying=true))
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))

  /** Entity class storing rows of table VariableSpecifications
   *  @param questionId Database column question_id SqlType(INT)
   *  @param questionType Database column question_type SqlType(INT)
   *  @param paramNumber Database column param_number SqlType(INT)
   *  @param specType Database column spec_type SqlType(INT)
   *  @param name Database column name SqlType(VARCHAR), Length(40,true)
   *  @param min Database column min SqlType(INT), Default(None)
   *  @param max Database column max SqlType(INT), Default(None)
   *  @param length Database column length SqlType(INT), Default(None)
   *  @param minLength Database column min_length SqlType(INT), Default(None)
   *  @param maxLength Database column max_length SqlType(INT), Default(None)
   *  @param genCode Database column gen_code SqlType(VARCHAR), Length(4000,true), Default(None) */
  case class VariableSpecificationsRow(questionId: Int, questionType: Int, paramNumber: Int, specType: Int, name: String, min: Option[Int] = None, max: Option[Int] = None, length: Option[Int] = None, minLength: Option[Int] = None, maxLength: Option[Int] = None, genCode: Option[String] = None)
  /** GetResult implicit for fetching VariableSpecificationsRow objects using plain SQL queries */
  implicit def GetResultVariableSpecificationsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[Option[String]]): GR[VariableSpecificationsRow] = GR{
    prs => import prs._
    VariableSpecificationsRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[String]))
  }
  /** Table description of table variable_specifications. Objects of this class serve as prototypes for rows in queries. */
  class VariableSpecifications(_tableTag: Tag) extends profile.api.Table[VariableSpecificationsRow](_tableTag, Some("video_quizzes"), "variable_specifications") {
    def * = (questionId, questionType, paramNumber, specType, name, min, max, length, minLength, maxLength, genCode) <> (VariableSpecificationsRow.tupled, VariableSpecificationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(questionId), Rep.Some(questionType), Rep.Some(paramNumber), Rep.Some(specType), Rep.Some(name), min, max, length, minLength, maxLength, genCode).shaped.<>({r=>import r._; _1.map(_=> VariableSpecificationsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column question_id SqlType(INT) */
    val questionId: Rep[Int] = column[Int]("question_id")
    /** Database column question_type SqlType(INT) */
    val questionType: Rep[Int] = column[Int]("question_type")
    /** Database column param_number SqlType(INT) */
    val paramNumber: Rep[Int] = column[Int]("param_number")
    /** Database column spec_type SqlType(INT) */
    val specType: Rep[Int] = column[Int]("spec_type")
    /** Database column name SqlType(VARCHAR), Length(40,true) */
    val name: Rep[String] = column[String]("name", O.Length(40,varying=true))
    /** Database column min SqlType(INT), Default(None) */
    val min: Rep[Option[Int]] = column[Option[Int]]("min", O.Default(None))
    /** Database column max SqlType(INT), Default(None) */
    val max: Rep[Option[Int]] = column[Option[Int]]("max", O.Default(None))
    /** Database column length SqlType(INT), Default(None) */
    val length: Rep[Option[Int]] = column[Option[Int]]("length", O.Default(None))
    /** Database column min_length SqlType(INT), Default(None) */
    val minLength: Rep[Option[Int]] = column[Option[Int]]("min_length", O.Default(None))
    /** Database column max_length SqlType(INT), Default(None) */
    val maxLength: Rep[Option[Int]] = column[Option[Int]]("max_length", O.Default(None))
    /** Database column gen_code SqlType(VARCHAR), Length(4000,true), Default(None) */
    val genCode: Rep[Option[String]] = column[Option[String]]("gen_code", O.Length(4000,varying=true), O.Default(None))

    /** Uniqueness Index over (questionId,questionType,paramNumber) (database name question_id) */
    val index1 = index("question_id", (questionId, questionType, paramNumber), unique=true)
  }
  /** Collection-like TableQuery object for table VariableSpecifications */
  lazy val VariableSpecifications = new TableQuery(tag => new VariableSpecifications(tag))
}
