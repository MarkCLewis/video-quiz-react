package models

import java.io.File
import java.io.PrintWriter
import sys.process._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import Tables._
import shared.SharedData.SubmissionResult
import ProblemSpec._

object ProblemSpecRunner {
  def checkResponse(spec: ProblemSpec, response: String): SubmissionResult = spec match {
    case MultipleChoice(id, prompt, options, correct) => 
      try {
        println(response.toInt, correct)
        SubmissionResult(response != null && response.toInt == correct, "Answered")
      } catch {
        case e: NumberFormatException => SubmissionResult(false, "Communication error, NPE.")
      }
    case WriteExpression(id, prompt, correctCode, varSpecs, generalSetup, numRuns) => 
      val code = s"""
        ${varSpecs.map(_.codeGenerator).mkString("\n")}
        $generalSetup
        if({$response} != {$correctCode}) sys.exit(${FailedTestExitCode})
        """
      ProblemSpecRunner.runCode(code, "", numRuns)
    case WriteFunction(id, prompt, correctCode, functionName, varSpecs, numRuns) => 
      val code = s"""
        ${correctCode.replaceAll(functionName+"\\(", functionName + "Correct(")}
        $response
        ${varSpecs.map(_.codeGenerator).mkString("\n")}
        val theirFunc = $functionName(${varSpecs.map(_.name).mkString(",")})
        val correctFunc = ${functionName}Correct(${varSpecs.map(_.name).mkString(",")})
        if(theirFunc != correctFunc) sys.exit(${FailedTestExitCode})
        """
      ProblemSpecRunner.runCode(code, "", numRuns)
    case WriteLambda(id, prompt, correctCode, returnType, varSpecs, numRuns) => 
      val funcType = s"(${varSpecs.map(_.typeName).mkString(", ")}) => $returnType"
      val args = varSpecs.map(_.name).mkString(", ")
      val code = s"""
        def tester(f1:$funcType, f2:$funcType):Unit = {
          ${varSpecs.map(_.codeGenerator).mkString("\n")}
          if(f1($args) != f2($args)) sys.exit(${FailedTestExitCode})
        }
        tester($response,$correctCode)
        """
      ProblemSpecRunner.runCode(code, "", numRuns)
  }

  def multipleChoice(id: Int, db: Database)(implicit ec: ExecutionContext): Future[MultipleChoice] = {
    val mcRow = db.run(MultipleChoiceQuestions.filter(_.mcQuestionId === id).result.head)
    mcRow.map(row => {
      val options = Seq(row.option1, row.option2) ++ Seq(row.option3, row.option4, row.option5, row.option6,
        row.option7, row.option8).filter(_.nonEmpty).map(_.get)
      MultipleChoice(row.mcQuestionId, row.prompt, options, row.correctOption)
    })
  }

  def writeFunction(id: Int, db: Database)(implicit ec: ExecutionContext): Future[WriteFunction] = {
    val funcRow = db.run(FunctionQuestions.filter(_.funcQuestionId === id).result.head)
    funcRow.flatMap(row => {
      val specs = db.run(VariableSpecifications.filter(vs => vs.questionId === id && vs.questionType === ProblemSpec.FunctionType)
        .sortBy(_.paramNumber).result).map(s => s.map(vs => VariableSpecBuilder(vs)))
      specs.map(s => WriteFunction(row.funcQuestionId, row.prompt, row.correctCode, row.functionName, s, row.numRuns))
    })
  }

  def writeLambda(id: Int, db: Database)(implicit ec: ExecutionContext): Future[WriteLambda] = {
    val lambdaRow = db.run(LambdaQuestions.filter(_.lambdaQuestionId === id).result.head)
    lambdaRow.flatMap(row => {
      val specs = db.run(VariableSpecifications.filter(vs => vs.questionId === id && vs.questionType === ProblemSpec.LambdaType)
        .sortBy(_.paramNumber).result).map(s => s.map(vs => VariableSpecBuilder(vs)))
      specs.map(s => WriteLambda(row.lambdaQuestionId, row.prompt, row.correctCode, row.returnType, s, row.numRuns))
    })
  }

  def writeExpression(id: Int, db: Database)(implicit ec: ExecutionContext): Future[WriteExpression] = {
    val exprRow = db.run(ExpressionQuestions.filter(_.exprQuestionId === id).result.head)
    exprRow.flatMap(row => {
      val specs = db.run(VariableSpecifications.filter(vs => vs.questionId === id && vs.questionType === ProblemSpec.ExpressionType)
        .sortBy(_.paramNumber).result).map(s => s.map(vs => VariableSpecBuilder(vs)))
      specs.map(s => WriteExpression(row.exprQuestionId, row.prompt, row.correctCode, s, row.generalSetup, row.numRuns))
    })
  }

  def apply(questionType: Int, id: Int, db: Database)(implicit ec: ExecutionContext): Future[ProblemSpec] = {
    questionType match {
      case ProblemSpec.MultipleChoiceType => // multiple choice
        multipleChoice(id, db)
      case ProblemSpec.FunctionType => // function
        writeFunction(id, db)
      case ProblemSpec.LambdaType => // lambda
        writeLambda(id, db)
      case ProblemSpec.ExpressionType => // expression
        writeExpression(id, db)
    }
  }
  
  def newWriteFunction(db:Database)(implicit ec: ExecutionContext):Future[WriteFunction] = {
    db.run(FunctionQuestions += FunctionQuestionsRow(0,"","","",10)).flatMap(_ =>
      db.run(FunctionQuestions.map(_.funcQuestionId).max.result)).flatMap(id =>
        writeFunction(id.get,db))
  } 
  
  def newWriteLambda(db:Database)(implicit ec: ExecutionContext):Future[WriteLambda] = {
    db.run(LambdaQuestions += LambdaQuestionsRow(0,"","","",10)).flatMap(_ =>
      db.run(LambdaQuestions.map(_.lambdaQuestionId).max.result)).flatMap(id =>
        writeLambda(id.get,db))
  } 
  
  def newWriteExpression(db:Database)(implicit ec: ExecutionContext):Future[WriteExpression] = {
    db.run(ExpressionQuestions += ExpressionQuestionsRow(0,"","","",10)).flatMap(_ =>
      db.run(ExpressionQuestions.map(_.exprQuestionId).max.result)).flatMap(id =>
        writeExpression(id.get,db))
  } 

  def nestTest(testCode: String, numRuns: Int): String = {
    s"""import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
    
Future {
   Thread.sleep(10000)
   sys.exit(${TimeoutExitCode})
}
for(i <- 1 to $numRuns) {
  $testCode
}
"""
  }

  def runCode(code: String, input: String, numRuns: Int): SubmissionResult = {
    val tmpFile = File.createTempFile("test", ".scala")
    tmpFile.deleteOnExit()
    val pw = new PrintWriter(tmpFile)
    val nestedCode = nestTest(code, numRuns)
    println(nestedCode)
    pw.println(nestedCode)
    pw.close
    val process = s"scala -J-Djava.security.manager -J-Djava.security.policy=mypolicy ${tmpFile.getAbsolutePath()}".run() 
    val ret = process.exitValue == 0
    println("Done running - " + ret)
    println("Exit value is " + process.exitValue)
    SubmissionResult(ret, process.exitValue() match {
      case CorrectSubmission => "Correct"
      case CompileErrorExitCode => "Compile/Runtime Error"
      case TimeoutExitCode => "Timeout"
      case FailedTestExitCode => "Test Case Failed"
    })
  }
}

/*

Parson's problem


case class IOCode(prompt: String) extends ProblemSpec {
  def checkResponse(response: String): Boolean = ???
}

case class CodeCompletion(prompt: String, code: String, varSpecs: Seq[VariableSpec], numRuns: Int) extends ProblemSpec {
  def checkResponse(response: String): Boolean = ???
}

case class CodeCompiles(prompt: String) extends ProblemSpec {
  def checkResponse(response: String): Boolean = ???
}

case class RegExMatching(prompt: String) extends ProblemSpec {
  def checkResponse(response: String): Boolean = ???
}

//case class CodeTracing(prompt:String) extends ProblemSpec {
//}

case class FileSubmission(prompt: String) extends ProblemSpec {
  def checkResponse(response: String): Boolean = ???
}
*/