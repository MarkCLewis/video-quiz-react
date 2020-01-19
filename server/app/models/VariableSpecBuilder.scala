package models

import Tables._

object VariableSpecBuilder {
  import VariableSpec._
  def apply(vs:VariableSpecificationsRow): VariableSpec = vs.specType match {
    case IntSpecType =>
      IntSpec(vs.paramNumber, vs.name, vs.min.getOrElse(0), vs.max.getOrElse(10))
    case DoubleSpecType =>
      DoubleSpec(vs.paramNumber, vs.name, vs.min.getOrElse(0).toDouble, vs.max.getOrElse(10).toDouble)
    case StringSpecType =>
      StringSpec(vs.paramNumber, vs.name, vs.length.getOrElse(5), vs.genCode.getOrElse(""))
    case IntListSpecType =>
      ListIntSpec(vs.paramNumber, vs.name, vs.minLength.getOrElse(2), vs.maxLength.getOrElse(10), vs.min.getOrElse(0), vs.max.getOrElse(10))
    case IntArraySpecType =>
      ArrayIntSpec(vs.paramNumber, vs.name, vs.minLength.getOrElse(2), vs.maxLength.getOrElse(10), vs.min.getOrElse(0), vs.max.getOrElse(10))
    case StringListSpecType =>
      ListStringSpec(vs.paramNumber, vs.name, vs.minLength.getOrElse(2), vs.maxLength.getOrElse(10), vs.length.getOrElse(5), vs.genCode.getOrElse(""))
    case IntArrayArraySpecType =>
      ArrayArrayIntSpec(vs.paramNumber, vs.name, vs.minLength.getOrElse(2), vs.maxLength.getOrElse(10), vs.minLength.getOrElse(2), vs.maxLength.getOrElse(10), vs.min.getOrElse(0), vs.max.getOrElse(10))
    case DoubleArrayArraySpecType =>
      ArrayArrayDoubleSpec(vs.paramNumber, vs.name, vs.minLength.getOrElse(2), vs.maxLength.getOrElse(10), vs.minLength.getOrElse(2), vs.maxLength.getOrElse(10), vs.min.getOrElse(0).toDouble, vs.max.getOrElse(10).toDouble)
  }

}