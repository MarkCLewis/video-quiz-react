package controllers

import javax.inject._

import shared.SharedData._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcCapabilities
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Future
import java.time.LocalDateTime
import models.Tables._
import java.sql.Timestamp

@Singleton
class Application @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {  

  def index = Action {
    Ok(views.html.index())
  }

  def tryLogin(username: String, password: String) = Action.async { implicit request =>
    models.Queries.validLogin(username, password, db).map { uid =>
      val ud = UserData(username, uid, scala.util.Random.nextInt())
      if (uid >= 0) Ok(Json.toJson(ud)).withSession("username" -> username, "uid" -> uid.toString) else Ok(Json.toJson(ud))
    }
  }

  def getCourses(userid: Int) = Action.async { implicit request =>
    println("Getting courses for " + userid)
    models.Queries.coursesFor(userid, db).map { crs =>
      val courses = crs.map { case (cr, role) => CourseData(cr.code+"-"+cr.section+"-"+cr.semester, cr.courseid) }
      Ok(Json.toJson(courses))
    }
  }

  def getQuizzes(courseid: Int) = Action.async { implicit request =>
    models.Queries.allQuizzesForClass(courseid, db).map { quizAndTime => 
      val quizData = quizAndTime.map { case (q, t) => QuizTimeData(q.quizid, q.name, q.description, t.toString) }
      Ok(Json.toJson(quizData))
    }
  }

  def getQuizData(quizid: Int, userid: Int) = Action.async { implicit request =>
    models.Queries.quizData(quizid, userid, db).map { qd =>
      Ok(Json.toJson(qd))
    }
  }

  def submitMC(quizid: Int, userid: Int, mcid: Int, selection: Int) = Action.async { implicit request =>
    models.ProblemSpecRunner.multipleChoice(mcid, db).flatMap { spec =>
      val c = models.ProblemSpecRunner.checkResponse(spec, selection.toString)
      db.run(McAnswers += McAnswersRow(Some(userid), Some(quizid), Some(mcid), selection, c, Timestamp.valueOf(LocalDateTime.now()))).map(_ => Ok(Json.toJson(c)))
    }
  }

  def submitCode(quizid: Int, userid: Int, codeid: Int, qtype: Int, code: String) = Action.async { implicit request =>
    models.ProblemSpecRunner(qtype, codeid, db).flatMap { spec =>
      val c = models.ProblemSpecRunner.checkResponse(spec, code)
      db.run(CodeAnswers += CodeAnswersRow(Some(userid), Some(quizid), codeid.toInt, qtype.toInt, code, c, Timestamp.valueOf(LocalDateTime.now()))).map(_ => Ok(Json.toJson(c)))
    }
  }
}
