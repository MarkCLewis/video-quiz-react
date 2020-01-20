package videoquizreact

import org.scalajs.dom

import slinky.core._
import slinky.web.ReactDOM
import slinky.web.html._

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    ReactDOM.render(
      TopComponent(),
      dom.document.getElementById("root")
    )

  }
}
