package videoquizreact

import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.core.annotations.react
import slinky.web.html._
import shared.SharedData._
import models._
import org.scalajs.dom
import org.scalajs.dom.experimental._
import play.api.libs.json._
import scala.concurrent.ExecutionContext
import scala.scalajs.js.URIUtils
import slinky.core.TagMod

@react class WriteCodeComponent extends Component {
  case class Props(question: CodeQuestionData, closed: Boolean, userData: UserData, quizTimeData: QuizTimeData, redoQuizOnSubmit: () => Unit)
  case class State(code: String, correct: Boolean, message: String)

  implicit val ec = ExecutionContext.global

  def initialState = State(props.question.lastCode.getOrElse(""), props.question.correct, "")

  def render(): ReactElement = div (
    br (),
    div ( "Coding", className := "problem-header" ),
    div ( props.question.spec.prompt.split("\n").map(line => div ( line ): TagMod[div.tag.type]):_*),
    div ( props.question.spec match {
      case wf: WriteFunction =>  "Function name: " + wf.functionName
      case wl: WriteLambda => "Return type: " + wl.returnType
      case _ => ""
    }),
    props.question.spec match {
      case wf: WriteFunction => VariableSpecComponent(wf.varSpecs)
      case we: WriteExpression => VariableSpecComponent(we.varSpecs)
      case wl: WriteLambda => VariableSpecComponent(wl.varSpecs)
      case _ => ""
    },
    textarea (onChange := { e => setState(state.copy(code = e.target.value))}, className := "code-area", disabled := props.closed || props.question.correct, value := state.code),
    br(),
    if (props.closed || props.question.correct) "" else button ( "Submit", onClick := (event => submitCode())),
    (if (state.correct) "Correct" else "Incorrect") + state.message
  )

  def submitCode(): Unit = {
    if (state.code.trim.nonEmpty) {
      val headers = new Headers()
      headers.set("Content-Type", "application/json")
      headers.set("Csrf-Token", dom.document.getElementsByTagName("body").apply(0).getAttribute("data-token"))
      Fetch.fetch(
        s"/submitCode",
        RequestInit(method = HttpMethod.POST, mode = RequestMode.cors, headers = headers, 
            body = Json.toJson((props.quizTimeData.id, props.userData.id, props.question.questionid, props.question.questionType, state.code)).toString())
      ).toFuture.flatMap { res =>
          res.text().toFuture
      }
      .map { res =>
        println("Code text response = " + res)
        Json.fromJson[SubmissionResult](Json.parse(res)) match {
          case JsSuccess(sr, path) =>
            println("after parse Boolean = " + sr)
            if (sr.correct) {
              setState(state.copy(correct = true, message = ""))
              props.redoQuizOnSubmit()
            } else {
              setState(state.copy(correct = false, message = " : " + sr.message))
            }
          case e @ JsError(_) =>
            println("Question Bundle error: " + e)
            // setState(_.copy(message = "Error with JSON response from server."))
        }
      }
    }
  }
}