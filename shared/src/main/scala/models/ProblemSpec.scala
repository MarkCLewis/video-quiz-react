package models

object ProblemSpec {
  val MultipleChoiceType = 0
  val FunctionType = 1
  val LambdaType = 2
  val ExpressionType = 3

  val CorrectSubmission = 0
  val CompileErrorExitCode = 1
  val TimeoutExitCode = 2
  val FailedTestExitCode = 3
}

sealed trait ProblemSpec {
  val id:Int
  val typeValue:Int
  val prompt: String
}

case class MultipleChoice(id: Int, prompt: String, options: Seq[String], correct: Int) extends ProblemSpec {
  val typeValue = ProblemSpec.MultipleChoiceType
}

case class WriteFunction(id: Int, prompt: String, correctCode: String, functionName: String, varSpecs: Seq[VariableSpec], numRuns: Int) extends ProblemSpec {
  val typeValue = ProblemSpec.FunctionType
}

case class WriteLambda(id: Int, prompt: String, correctCode: String, returnType: String, varSpecs: Seq[VariableSpec], numRuns: Int) extends ProblemSpec {
  val typeValue = ProblemSpec.LambdaType
}

case class WriteExpression(id: Int, prompt: String, correctCode: String, varSpecs: Seq[VariableSpec], generalSetup: String, numRuns: Int) extends ProblemSpec {
  val typeValue = ProblemSpec.ExpressionType
}
