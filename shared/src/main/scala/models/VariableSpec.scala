package models

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.concurrent.Await

/**
 * @author mlewis
 */
object VariableType extends Enumeration {
  type Type = Value
  val Int, Double, String = Value
}

object VariableSpec {
  val IntSpecType = 0
  val DoubleSpecType = 1
  val StringSpecType = 2
  val IntListSpecType = 3
  val IntArraySpecType = 4
  val StringListSpecType = 5
  val IntArrayArraySpecType = 6
  val DoubleArrayArraySpecType = 7
  
}

sealed trait VariableSpec {
  val name: String
  val typeName: String
  val typeNumber: Int
  val paramNumber: Int
  def codeGenerator(): String // Return a string that is code to generate this value. 
}

case class IntSpec(paramNumber:Int, name: String, min: Int, max: Int) extends VariableSpec {
  val typeName = "Int"
  val typeNumber = VariableSpec.IntSpecType

  def codeGenerator(): String = {
    s"val $name = util.Random.nextInt(($max)-($min))+($min)"
  }
}

case class DoubleSpec(paramNumber:Int, name: String, min: Double, max: Double) extends VariableSpec {
  val typeName = "Double"
  val typeNumber = VariableSpec.DoubleSpecType

  def codeGenerator(): String = {
    s"val $name = math.random*(($max)-($min))+($min)"
  }
}

case class StringSpec(paramNumber:Int, name: String, length: Int, genCode: String) extends VariableSpec {
  val typeName = "String"
  val typeNumber = VariableSpec.StringSpecType

  def codeGenerator(): String = {
    if(genCode.isEmpty())
      s"val $name = (for(i <- 0 until $length) yield { ('a'+util.Random.nextInt(26)).toChar }).mkString"
    else
      s"val $name = $genCode"
  }
}

case class ListIntSpec(paramNumber:Int, name: String, minLen:Int, maxLen:Int, min: Int, max: Int) extends VariableSpec {
  val typeName = "List[Int]"
  val typeNumber = VariableSpec.IntListSpecType

  def codeGenerator(): String = {
    s"val $name = List.fill(util.Random.nextInt(($maxLen)-($minLen))+($minLen))(util.Random.nextInt(($max)-($min))+($min))"
  }
}

case class ArrayIntSpec(paramNumber:Int, name: String, minLen:Int, maxLen:Int, min: Int, max: Int) extends VariableSpec {
  val typeName = "Array[Int]"
  val typeNumber = VariableSpec.IntArraySpecType

  def codeGenerator(): String = {
    s"val $name = Array.fill(util.Random.nextInt(($maxLen)-($minLen))+($minLen))(util.Random.nextInt(($max)-($min))+($min))"
  }
}

case class ListStringSpec(paramNumber:Int, name: String, minLen:Int, maxLen:Int, stringLength: Int, genCode: String) extends VariableSpec {
  val typeName = "List[String]"
  val typeNumber = VariableSpec.StringListSpecType

  def codeGenerator(): String = {
    if(genCode.isEmpty())
      s"val $name = List.fill(util.Random.nextInt(($maxLen)-($minLen))+($minLen)){(for(i <- 0 until $stringLength) yield { ('a'+util.Random.nextInt(26)).toChar }).mkString}"
    else
      s"val $name = List.fill(util.Random.nextInt(($maxLen)-($minLen))+($minLen)){$genCode}"
  }
}

case class ArrayArrayIntSpec(paramNumber:Int, name: String, minLen1:Int, maxLen1:Int, minLen2:Int, maxLen2:Int, min: Int, max: Int) extends VariableSpec {
  val typeName = "Array[Array[Int]]"
  val typeNumber = VariableSpec.IntArrayArraySpecType

  def codeGenerator(): String = {
    s"val $name = Array.fill(util.Random.nextInt(($maxLen1)-($minLen1))+($minLen1),"+
      s"util.Random.nextInt(($maxLen2)-($minLen2))+($minLen2))(util.Random.nextInt(($max)-($min))+($min))"
  }
}

case class ArrayArrayDoubleSpec(paramNumber:Int, name: String, minLen1:Int, maxLen1:Int, minLen2:Int, maxLen2:Int, min: Double, max: Double) extends VariableSpec {
  val typeName = "Array[Array[Double]]"
  val typeNumber = VariableSpec.DoubleArrayArraySpecType

  def codeGenerator(): String = {
    s"val $name = Array.fill(util.Random.nextInt(($maxLen1)-($minLen1))+($minLen1),"+
      s"util.Random.nextInt(($maxLen2)-($minLen2))+($minLen2))(math.random*(($max)-($min))+($min))"
  }
}

