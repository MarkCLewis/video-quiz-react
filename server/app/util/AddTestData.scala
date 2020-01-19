package util

import slick.jdbc.MySQLProfile.api._
import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import models._
import Tables._
import java.sql.Timestamp
import java.time.LocalDateTime

/**
 * @author mlewis
 */
object AddTestData extends App {
    println("Get database")
//    val db = Database.forConfig("slick.dbs.default")
    val db = Database.forURL("jdbc:mysql://localhost/video_quizzes", user="mlewis", password="password", driver="com.mysql.cj.jdbc.Driver")
    Await.result(db.run(DBIO.seq(
      Users.delete,
      Courses.delete,
      UserCourseAssoc.delete,
      Quizzes.delete,
      QuizCourseCloseAssoc.delete,
      MultipleChoiceQuestions.delete,
      MultipleChoiceAssoc.delete,
      FunctionQuestions.delete,
      FunctionAssoc.delete,
      McAnswers.delete,
      CodeAnswers.delete,
      VariableSpecifications.delete
    )), Duration.Inf)
    Await.result(db.run(DBIO.seq(
      sqlu"ALTER TABLE users AUTO_INCREMENT = 1;",
      sqlu"ALTER TABLE courses AUTO_INCREMENT = 1;",
      sqlu"ALTER TABLE quizzes AUTO_INCREMENT = 1;",
      sqlu"ALTER TABLE multiple_choice_questions AUTO_INCREMENT = 1;",
      sqlu"ALTER TABLE function_questions AUTO_INCREMENT = 1;"
    )), Duration.Inf)
    Await.result(db.run(DBIO.seq(
      Users += UsersRow(0,"mlewist","0123456"),
      Users += UsersRow(0,"mlewis","0123456"),
      Courses += CoursesRow(0,"CSCI1302","F15",6),
      UserCourseAssoc += UserCourseAssocRow(Some(1),Some(1),shared.SharedData.Student),
      UserCourseAssoc += UserCourseAssocRow(Some(2),Some(1),shared.SharedData.Instructor),
      Quizzes += QuizzesRow(0, "Quiz #1", "A test quiz."),
      Quizzes += QuizzesRow(0, "Quiz #2", "A test quiz."),
      QuizCourseCloseAssoc += QuizCourseCloseAssocRow(Some(2),Some(1),Timestamp.valueOf("2015-12-12 12:12:12")),
      QuizCourseCloseAssoc += QuizCourseCloseAssocRow(Some(1),Some(1),Timestamp.valueOf("2015-06-12 12:12:12")),
      MultipleChoiceQuestions += MultipleChoiceQuestionsRow(0,"MC question 1","true","false",None,None,None,None,None,None,1),
      MultipleChoiceQuestions += MultipleChoiceQuestionsRow(0,"MC question 2","blue","red",Some("green"),None,None,None,None,None,2),
      MultipleChoiceQuestions += MultipleChoiceQuestionsRow(0,"MC question 3","mammal","bird",Some("fish"),None,None,None,None,None,3),
      MultipleChoiceAssoc += MultipleChoiceAssocRow(Some(1),Some(1)),
      MultipleChoiceAssoc += MultipleChoiceAssocRow(Some(1),Some(2)),
      MultipleChoiceAssoc += MultipleChoiceAssocRow(Some(2),Some(3)),
      FunctionQuestions += FunctionQuestionsRow(0,"Function question 1","good code","foo",10),
      FunctionQuestions += FunctionQuestionsRow(0,"Write a function called bar() that returns 42.","def bar() = 42","bar",10),
      FunctionQuestions += FunctionQuestionsRow(0,"Write a factorial function called fact.","def fact(n:Int) = (1 to n).product","fact",10),
      VariableSpecifications += VariableSpecificationsRow(3,1,0,0,"n",Some(0),Some(10)),
      FunctionAssoc += FunctionAssocRow(Some(1),Some(1)),
      FunctionAssoc += FunctionAssocRow(Some(2),Some(2)),
      FunctionAssoc += FunctionAssocRow(Some(2),Some(3)),
      McAnswers += McAnswersRow(Some(1),Some(1),Some(1),0,true, Timestamp.valueOf(LocalDateTime.now())),
      McAnswers += McAnswersRow(Some(1),Some(1),Some(2),0,false, Timestamp.valueOf(LocalDateTime.now())),
      CodeAnswers += CodeAnswersRow(Some(1),Some(1),1,1,"code",false, Timestamp.valueOf(LocalDateTime.now())),
      CodeAnswers += CodeAnswersRow(Some(1),Some(1),1,1,"code",true, Timestamp.valueOf(LocalDateTime.now()))
    )), Duration.Inf)
    db.close()
  
  
}