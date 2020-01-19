package videoquizreact

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html._
import shared.SharedData._
import org.scalajs.dom.experimental.Fetch
import scala.scalajs.js.Thenable.Implicits._
import play.api.libs.json._
import scala.scalajs.js.JSON
import scala.concurrent.ExecutionContext

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
    println("Loading courses")
    Fetch.fetch(s"/getCourses?userid=${props.userData.id}")
      .flatMap { res =>
        println("Got courses.")
        println(res)
        res.text()
      }
      .map { res =>
        println("Got courses result")
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