package com.beben.tool.openeva.xscaid.pressuretest

import java.util.concurrent.TimeUnit

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model._
import com.beben.tool.openeva.xscaid.service.{AnswerService, AnswersheetService, LoginService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
@Scope("prototype")
class ClientMocker(@Autowired private val xSCAidConfiguration: XSCAidConfiguration,
                   @Autowired private val restTemplate: RestTemplate,
                   @Autowired loginService: LoginService,
                   @Autowired answerService: AnswerService,
                   @Autowired answerSheetService: AnswersheetService) {

  private var personalExamContext: PersonalExamContext = _

  def mock(foreignId: String): Unit = {

    personalExamContext =
      loginService.loginByVCode(foreignId, xSCAidConfiguration.getGodVcode)

    personalExamContext.getTests.forEach(
      mockTest(_, personalExamContext.getUserId,
        xSCAidConfiguration.getAnswerSubmissionRepeat,
        xSCAidConfiguration.getAnswerCommitDelay)
    )
  }

  private def mockTest(testInfo: PersonalTestInfo, userId: String,
                       answerSubmissionRepeat: Int, answerCommitDelay: Long): Unit = {

    import scala.collection.JavaConverters._

    val curTime = System.currentTimeMillis()

    val answers = testInfo.getTestPaper.getQuestionSets.asScala.flatMap(
      _.getQuestions.asScala).map(question => {

      val answer = new Answer

      answer.setRefQuestion(question.getId)

      val opLog = new OpLog
      opLog.setStartTime(curTime)
      opLog.setEndTime(curTime)
      opLog.setMisc("Mocked misc.")
      answer.setOplog(opLog)

      val choiceResult = new ChoiceResult
      choiceResult.setContent("Mocked content.")
      choiceResult.setStyle("Mocked style.")
      answer.setResult(choiceResult)

      answer
    })

    0.until(answerSubmissionRepeat).foreach(_ =>

      answers.foreach(answer => {

        val answerSubmissionResponse =
          answerService.submitAnswer(testInfo.getTestId, userId, answer)

        assert(answerSubmissionResponse.getStatusCode == HttpStatus.OK)

        TimeUnit.MICROSECONDS.sleep(answerCommitDelay)
      })
    )

    val answerSheet = new AnswerSheet
    answerSheet.setAnswers(answers.asJava)
    val opLog = new OpLog
    opLog.setStartTime(curTime)
    opLog.setEndTime(curTime)
    opLog.setMisc("Mocked misc.")
    answerSheet.setOplog(opLog)
    answerSheet.setRefAAdmissionTicket(testInfo.getTicketId)

    val answerSheetSubmissionResponse =
      answerSheetService.submitAnswersheet(testInfo.getTicketId, answerSheet)
    assert(answerSheetSubmissionResponse.getStatusCode == HttpStatus.OK)
  }
}
