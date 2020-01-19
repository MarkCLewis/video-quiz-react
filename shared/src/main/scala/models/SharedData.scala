package shared

import play.api.libs.json._
import models._

object SharedData {
  val Student = 1
  val Instructor = 2

  case class UserData(username: String, id: Int, sessionUID: Int)
  case class CourseData(name: String, id: Int, role: Int)
  case class QuizTimeData(id: Int, name: String, description: String, time: String)

  implicit val userDataReads = Json.reads[UserData]
  implicit val userDataWrites = Json.writes[UserData]
  implicit val userDataFormat = Json.format[UserData]
  
  implicit val isReads = Json.reads[IntSpec]
  implicit val isWrites = Json.writes[IntSpec]
  implicit val isFormat = Json.format[IntSpec]

  implicit val dsReads = Json.reads[DoubleSpec]
  implicit val dsWrites = Json.writes[DoubleSpec]
  implicit val dsFormat = Json.format[DoubleSpec]

  implicit val ssReads = Json.reads[StringSpec]
  implicit val ssWrites = Json.writes[StringSpec]
  implicit val ssFormat = Json.format[StringSpec]

  implicit val lisReads = Json.reads[ListIntSpec]
  implicit val lisWrites = Json.writes[ListIntSpec]
  implicit val lisFormat = Json.format[ListIntSpec]

  implicit val aisReads = Json.reads[ArrayIntSpec]
  implicit val aisWrites = Json.writes[ArrayIntSpec]
  implicit val aisFormat = Json.format[ArrayIntSpec]

  implicit val lssReads = Json.reads[ListStringSpec]
  implicit val lssWrites = Json.writes[ListStringSpec]
  implicit val lssFormat = Json.format[ListStringSpec]

  implicit val aaisReads = Json.reads[ArrayArrayIntSpec]
  implicit val aaisWrites = Json.writes[ArrayArrayIntSpec]
  implicit val aaisFormat = Json.format[ArrayArrayIntSpec]

  implicit val aadsReads = Json.reads[ArrayArrayDoubleSpec]
  implicit val aadsWrites = Json.writes[ArrayArrayDoubleSpec]
  implicit val aadsFormat = Json.format[ArrayArrayDoubleSpec]

  implicit val vsReads = Json.reads[VariableSpec]
  implicit val vsWrites = Json.writes[VariableSpec]
  implicit val vsFormat = Json.format[VariableSpec]

  implicit val mcReads = Json.reads[MultipleChoice]
  implicit val mcWrites = Json.writes[MultipleChoice]
  implicit val mcFormat = Json.format[MultipleChoice]

  implicit val wfReads = Json.reads[WriteFunction]
  implicit val wfWrites = Json.writes[WriteFunction]
  implicit val wfFormat = Json.format[WriteFunction]

  implicit val weReads = Json.reads[WriteExpression]
  implicit val weWrites = Json.writes[WriteExpression]
  implicit val weFormat = Json.format[WriteExpression]

  implicit val wlReads = Json.reads[WriteLambda]
  implicit val wlWrites = Json.writes[WriteLambda]
  implicit val wlFormat = Json.format[WriteLambda]

  implicit val psReads = Json.reads[ProblemSpec]
  implicit val psWrites = Json.writes[ProblemSpec]
  implicit val psFormat = Json.format[ProblemSpec]

  implicit val mcdReads = Json.reads[MultipleChoiceData]
  implicit val mcdWrites = Json.writes[MultipleChoiceData]
  implicit val mcdFormat = Json.format[MultipleChoiceData]

  implicit val cqdReads = Json.reads[CodeQuestionData]
  implicit val cqdWrites = Json.writes[CodeQuestionData]
  implicit val cqdFormat = Json.format[CodeQuestionData]

  implicit val quizDataReads = Json.reads[QuizData]
  implicit val quizDataWrites = Json.writes[QuizData]
  implicit val quizDataFormat = Json.format[QuizData]

  implicit val quizTimeDataReads = Json.reads[QuizTimeData]
  implicit val quizTimeDataWrites = Json.writes[QuizTimeData]
  implicit val quizTimeDataFormat = Json.format[QuizTimeData]

  implicit val courseDataReads = Json.reads[CourseData]
  implicit val courseDataWrites = Json.writes[CourseData]
  implicit val courseDataFormat = Json.format[CourseData]
}