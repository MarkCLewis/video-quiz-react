package videoquizreact

import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.core.annotations.react
import slinky.web.html._
import shared.SharedData._
import models._

@react class LambdaComponent extends Component {
  case class Props(question: CodeQuestionData, closed: Boolean, userData: UserData, quizTimeData: QuizTimeData, redoQuizOnSubmit: () => Unit)
  case class State(expanded: Boolean)

  def initialState = State(false)

  def render(): ReactElement = {
    "Lambda"
  }
}