package videoquizreact

import org.scalajs.dom
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.core.annotations.react
import slinky.web.html._
import shared.SharedData._
import models._
import org.scalajs.dom.experimental._
import play.api.libs.json._
import scala.concurrent.ExecutionContext
import slinky.core.TagMod

@react class MultipleChoiceComponent extends Component {
  case class Props(question: MultipleChoiceData, closed: Boolean, userData: UserData, quizTimeData: QuizTimeData, redoQuizOnSubmit: () => Unit)
  case class State(selected: Option[Int], answered: Boolean)

  implicit val ec = ExecutionContext.global

  def initialState = State(props.question.answer, props.question.answer.nonEmpty)

  def render(): ReactElement = {
    val qid = props.question.mcid
    println(props.question)
    div (
      br (),
      div ( "Multiple Choice", className := "problem-header" ),
      div ( props.question.spec.prompt.split("\n").map(line => div ( line ): TagMod[div.tag.type]):_*),
      props.question.spec.options.zipWithIndex.map { case (opt, i) =>
        div (
          input ( 
            `type` := "radio", 
            id := "mc-"+qid+"-"+i, 
            name := "mc-"+qid, 
            value := i.toString(), 
            checked := i == state.selected.getOrElse(-1),
            onChange := (event => if (event.target.checked) setState(state.copy(selected = Some(i)))),
            disabled := props.closed || props.question.answer.nonEmpty
          ),
          label ( opt ),
          key := "mcopt-"+i
        )
      },
      if (props.closed || props.question.answer.nonEmpty || state.answered) 
        (if (props.question.answer.getOrElse(-100) == props.question.spec.correct) "Correct" else "Incorrect") 
        else button ( "Submit", onClick := (event => submitAnswer()))
    )
  }

  def submitAnswer(): Unit = {
    val headers = new Headers()
    headers.set("Content-Type", "application/json")
    headers.set("Csrf-Token", dom.document.getElementsByTagName("body").apply(0).getAttribute("data-token"))
    state.selected.foreach { sel =>
      Fetch.fetch(
        s"/submitMC",
        RequestInit(method = HttpMethod.POST, mode = RequestMode.cors, headers = headers, body = Json.toJson((props.quizTimeData.id, props.userData.id, props.question.mcid, sel + 1)).toString())
      ).toFuture.flatMap { res =>
          res.text().toFuture
        }
        .map { res =>
          Json.fromJson[SubmissionResult](Json.parse(res)) match {
            case JsSuccess(sr, path) =>
              if (sr.correct) {
                props.redoQuizOnSubmit()
              } else {
                setState(state.copy(answered = true))
              }
            case e @ JsError(_) =>
              println("Question Bundle error: " + e)
              // setState(_.copy(message = "Error with JSON response from server."))
          }
        }
    }
  }
}