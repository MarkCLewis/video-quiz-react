package videoquizreact

import slinky.core.StatelessComponent
import slinky.core.facade.ReactElement
import slinky.core.annotations.react
import slinky.web.html._
import shared.SharedData._

@react class QuizList extends StatelessComponent {
  case class Props(userData: UserData, quizData: Seq[QuizTimeData])

  def render(): ReactElement = {
    table (
      thead ( tr ( th ("Quiz"), th ( "Score" ), th ("Close Time"))),
      tbody (
        props.quizData.map(q => QuizComponent(props.userData, q).withKey(q.name))
      ),
      className := "quiz-table"
    )
  }
}