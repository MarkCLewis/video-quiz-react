package models

import Tables._
import java.sql.Timestamp

/**
 * @author mlewis
 */
case class NewCourseData(code:String,semester:String,section:Int,instructorNames:String,studentData:String)

case class StudentData(userid:Int, username:String, correct:Int)

case class CourseQuizData(row:QuizzesRow,time:Timestamp)

case class CourseData(row:CoursesRow, instructors:Seq[UsersRow], students:Seq[StudentData], quizzes:Seq[CourseQuizData], totalQuestions:Int)