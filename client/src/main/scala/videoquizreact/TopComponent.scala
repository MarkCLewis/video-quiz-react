package videoquizreact

import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import shared.SharedData._

@react class TopComponent extends Component {
  type Props = Unit
  case class State(userData: Option[UserData])

  def initialState = State(None)

  def render(): ReactElement = div ({
    val ret = state.userData.map(ud => 
      if (ud.instructor) InstructorPage(ud, doLogout) else UserPage(ud, doLogout)
    ).getOrElse(LoginPage(doLogin(_)))
    ret
  })

  def doLogin(ud: UserData): Unit = {
    setState(State(Some(ud)))
  }

  def doLogout(): Unit = {
    setState(State(None))
  }
}