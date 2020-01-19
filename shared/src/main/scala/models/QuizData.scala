package models

/**
 * @author mlewis
 */
case class QuizData(
  quizid: Int,
  userid: Int,
  name: String,
  description: String,
  multipleChoice: Seq[MultipleChoiceData],
  codeQuestions: Seq[CodeQuestionData])

case class MultipleChoiceData(
  mcid: Int,
  spec: MultipleChoice,
  answer: Option[Int])

case class CodeQuestionData(
    questionid: Int,
    questionType: Int,
    spec: ProblemSpec,
    lastCode: Option[String],
    correct: Boolean) {
  def typeString = questionType match {
    case 1 => "function"
    case 2 => "lambda"
    case 3 => "expression"
  }
}

case class QuizResultsData(
  quizid: Int,
  courseid: Int,
  name: String,
  description: String,
  multipleChoiceResults: Seq[MultipleChoiceResultsData],
  codeQuestionResults: Seq[CodeQuestionResultsData])

case class MultipleChoiceResultsData(
  spec: MultipleChoice,
  answerCount: Seq[Int])

case class CodeQuestionResultsData(
  prompt:String,
  numSubmissions:Int,
  numStudentsSubmitting:Int,
  numCorrect:Int)