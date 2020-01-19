package videoquizreact

import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.core.annotations.react
import slinky.web.html._
import shared.SharedData._
import scala.scalajs.js.Date
import org.scalajs.dom.experimental.Fetch
import play.api.libs.json._
import scala.concurrent.ExecutionContext
import slinky.core.TagMod
import models._

@react class QuizComponent extends Component {
  case class Props(userData: UserData, quizTimeData: QuizTimeData)
  case class State(expanded: Boolean, quizData: Option[QuizData])

  implicit val ec = ExecutionContext.global

  def initialState = State(false, None)

  override def componentDidMount() = loadQuestions()

  def render(): ReactElement = {
    val closed = new Date(props.quizTimeData.time).getTime() < new Date().getTime()
    tr ( 
      td ( 
        if (state.expanded) {
          div (
            props.quizTimeData.name, span(" (hide)", onClick := (event => setState(_.copy(expanded = false)))),
            state.quizData.map { qd =>
              div (
                (qd.multipleChoice.map { mcd => MultipleChoiceComponent(mcd, closed, props.userData, props.quizTimeData, loadQuestions).withKey("mc-"+mcd.mcid): TagMod[div.tag.type] } ++
                qd.codeQuestions.map { cqd => WriteCodeComponent(cqd, closed, props.userData, props.quizTimeData, loadQuestions).withKey("fc-"+cqd.questionid): TagMod[div.tag.type] }):_*
              ): TagMod[div.tag.type]
            }.getOrElse(div ( "Loading..." ): TagMod[div.tag.type])
          )
        } else {
          div (
            props.quizTimeData.name, span(" (view)", onClick := (event => setState(_.copy(expanded = true))))
          )
        }
      ), 
      state.quizData.map(qd => 
        td ((qd.multipleChoice.count(d => d.answer.getOrElse(-100) + 1 == d.spec.correct) + qd.codeQuestions.count(_.correct)) + "/" + 
            (qd.codeQuestions.length + qd.multipleChoice.length)): TagMod[tr.tag.type]
      ).getOrElse(td ( "?/?" ): TagMod[tr.tag.type]), 
      td (
        if (closed) "Closed" else props.quizTimeData.time
      )
    )
  }

  def loadQuestions(): Unit = {
    Fetch.fetch(s"/getQuizData?quizid=${props.quizTimeData.id}&userid=${props.userData.id}").toFuture
      .flatMap { res =>
        res.text().toFuture
      }
      .map { res =>
        Json.fromJson[QuizData](Json.parse(res)) match {
          case JsSuccess(qd, path) =>
            setState(state.copy(quizData = Some(qd)))
          case e @ JsError(_) =>
            println("Question Bundle error: " + e)
            // setState(_.copy(message = "Error with JSON response from server."))
        }
      }

  }
}