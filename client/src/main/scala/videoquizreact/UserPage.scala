package videoquizreact

import org.scalajs.dom
import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html._
import shared.SharedData._
import scala.scalajs.js.Thenable.Implicits._
import play.api.libs.json._
import scala.scalajs.js.JSON
import scala.concurrent.ExecutionContext
import org.scalajs.dom.experimental._

@react class UserPage extends Component {
  case class Props(userData: UserData, doLogout: () => Unit)
  case class State(courses: Seq[CourseData])

  override def componentDidMount() = loadCourses()

  def initialState = State(Nil)

  def render(): ReactElement = div (
    header (h1 ("User page: " + props.userData.username) ),
    hr(),
    state.courses.map(cd => CourseComponent(props.userData, cd).withKey(cd.name)),
    hr(),
    footer ( button("Logout", id := "logout", onClick := (event => props.doLogout())) )
  )

  implicit val ec = ExecutionContext.global

  def loadCourses(): Unit = {
    val headers = new Headers()
    headers.set("Content-Type", "application/json")
    headers.set("Csrf-Token", dom.document.getElementsByTagName("body").apply(0).getAttribute("data-token"))
    Fetch.fetch(
      s"/getCourses?userid=${props.userData.id}",
      RequestInit(method = HttpMethod.POST, mode = RequestMode.cors, headers = headers, body = Json.toJson(props.userData.id).toString())
    ).flatMap { res =>
        res.text()
      }
      .map { res =>
        Json.fromJson[Seq[CourseData]](Json.parse(res)) match {
          case JsSuccess(cd, path) =>
            println(cd)
            setState(State(cd))
          case e @ JsError(_) =>
            println("Courses error: " + e)
            // setState(_.copy(message = "Error with JSON response from server."))
        }
      }
  }
}