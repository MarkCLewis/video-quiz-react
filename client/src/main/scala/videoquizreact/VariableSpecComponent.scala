package videoquizreact

import slinky.core.StatelessComponent
import models.VariableSpec
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.div
import slinky.web.html.key
import slinky.web.html.ul
import slinky.web.html.li

@react class VariableSpecComponent extends StatelessComponent {
  case class Props(vspecs: Seq[VariableSpec])

  def render(): ReactElement = if (props.vspecs.nonEmpty) div (
    "Variables/Parameters:",
    ul (
      props.vspecs.map(vs => li ( s"${vs.name}: ${vs.typeName}", key := vs.name ))
    )
  ) else ""
}