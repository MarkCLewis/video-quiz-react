package videoquizreact

import org.scalajs.dom
import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html._
import org.scalajs.dom.experimental._

import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.js.JSON
import shared.SharedData._

import scala.concurrent.ExecutionContext
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError

@react class LoginPage extends Component {
  case class Props(doLogin: UserData => Unit)
  case class State(username: Option[String], password: Option[String], message: String)

  def initialState = State(None, None, "")

  def render(): ReactElement = div (
    h1 ("Login"),
    div (
      "Username: ", input(`type` := "text", id := "name", value := state.username.getOrElse(""), onChange := (e => setState(state.copy(username = Some(e.target.value)))))
    ),
    div (
      "Password", input(`type` := "password", id := "password", value := state.password.getOrElse(""), onChange := (e => setState(state.copy(password = Some(e.target.value)))))
    ),
    div (
      button("Login", onClick := (event => tryLogin()))
    ),
    div (
      state.message
    )
  )

  implicit val ec = ExecutionContext.global
  
  def tryLogin(): Unit = {
    if (state.username.isEmpty) setState(state.copy(message = "Username is required."))
    else if (state.password.isEmpty) setState(state.copy(message = "Password is required."))
    else {
      val headers = new Headers()
      headers.set("Content-Type", "application/json")
      headers.set("Csrf-Token", dom.document.getElementsByTagName("body").apply(0).getAttribute("data-token"))
      Fetch.fetch(
        s"/tryLogin",
        RequestInit(method = HttpMethod.POST, mode = RequestMode.cors, headers = headers, body = Json.toJson(LoginInfo(state.username.get, state.password.get)).toString())
      ).flatMap(_.text()).map { res =>
        println(state.username, state.password)
        Json.fromJson[UserData](Json.parse(res)) match {
          case JsSuccess(ud, path) =>
            println(ud)
            if (ud.id < 0) {
              setState(_.copy(message = "Invalid username or password."))
            } else {
              props.doLogin(ud)
            }
          case e @ JsError(_) =>
            setState(_.copy(message = "Error with JSON response from server."))
        }
      }
    }
  }
}