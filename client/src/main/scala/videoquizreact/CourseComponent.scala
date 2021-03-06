package videoquizreact

import org.scalajs.dom
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.core.annotations.react
import slinky.web.html._
import shared.SharedData._
import scala.concurrent.ExecutionContext
import org.scalajs.dom.experimental._
import play.api.libs.json._
import models._

@react class CourseComponent extends Component {
  case class Props(userData: UserData, courseData: CourseData)
  case class State(expanded: Boolean, quizTimeData: Seq[QuizTimeData])

  def initialState = State(false, Nil)

  override def componentDidMount() = loadQuizzes()

  def render(): ReactElement = {
    if (state.expanded) {
      div (
        props.courseData.name,
        span ("(click to collapse)", onClick := (event => setState(_.copy(expanded = false)))),
        br(),
        QuizList(props.userData, state.quizTimeData)
      )
    } else {
      div (props.courseData.name, span ("(click to expand)", onClick := (event => setState(_.copy(expanded = true)))))
    }
  }

  implicit val ec = ExecutionContext.global

  def loadQuizzes(): Unit = {
    val headers = new Headers()
    headers.set("Content-Type", "application/json")
    headers.set("Csrf-Token", dom.document.getElementsByTagName("body").apply(0).getAttribute("data-token"))
    Fetch.fetch(
      s"/getQuizzes",
      RequestInit(method = HttpMethod.POST, mode = RequestMode.cors, headers = headers, body = Json.toJson(props.courseData.id).toString())
    ).toFuture.flatMap { res =>
        res.text().toFuture
      }
      .map { res =>
        println("Got courses result")
        Json.fromJson[Seq[QuizTimeData]](Json.parse(res)) match {
          case JsSuccess(qtd, path) =>
            println(qtd)
            setState(State(true, qtd))
          case e @ JsError(_) =>
            println("Quizzes error: " + e)
            // setState(_.copy(message = "Error with JSON response from server."))
        }
      }
  }

}