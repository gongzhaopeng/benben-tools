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

import scala.util.Random
import scala.collection.JavaConverters._

@Component
@Scope("prototype")
class ClientMocker(@Autowired private val xSCAidConfiguration: XSCAidConfiguration,
                   @Autowired private val restTemplate: RestTemplate,
                   @Autowired loginService: LoginService,
                   @Autowired answerService: AnswerService,
                   @Autowired answerSheetService: AnswersheetService) {

  private var personalExamContext: PersonalExamContext = _

  var foreignId: String = _

  private var userLevel: Int = _

  private var testAnswersArray: Array[List[Answer]] = _
  private var testAnswerSheetArray: Array[AnswerSheet] = _
  private var testAnswerSubmitDelayArray: Array[List[Long]] = _

  def init(foreignId: String): Unit = {

    this.foreignId = foreignId

    val random = new Random()

    userLevel = random.nextInt(xSCAidConfiguration.getUserLevelCount)

    personalExamContext =
      loginService.loginByVCode(foreignId, xSCAidConfiguration.getGodVcode)

    val tests = personalExamContext.getTests.asScala

    testAnswersArray = new Array[List[Answer]](tests.length)
    testAnswerSheetArray = new Array[AnswerSheet](tests.length)
    testAnswerSubmitDelayArray = new Array[List[Long]](tests.length)

    val actualAnswerSubmissionRepeat =
      (1 - xSCAidConfiguration.getAnswersCountRatioGap * userLevel) *
        xSCAidConfiguration.getAnswerSubmissionRepeat

    tests.zipWithIndex.foreach {
      case (testInfo, index) =>
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

        testAnswersArray(index) =
          Range(0, actualAnswerSubmissionRepeat.ceil.toInt)
            .map(_ => answers).reduce(_ ++ _)
            .slice(0, (answers.size * actualAnswerSubmissionRepeat).ceil.toInt)
            .toList

        val answerSheet = new AnswerSheet
        answerSheet.setAnswers(answers.asJava)
        val opLog = new OpLog
        opLog.setStartTime(curTime)
        opLog.setEndTime(curTime)
        opLog.setMisc("Mocked misc.")
        answerSheet.setOplog(opLog)
        answerSheet.setRefAAdmissionTicket(testInfo.getTicketId)
        testAnswerSheetArray(index) = answerSheet
    }

    var testTimeRatioList = Range(0, tests.length - 1).map(_ =>
      1 - (random.nextDouble() * 2 - 1) * xSCAidConfiguration.getTestTimeDeviation)
      .toList
    val finalTestTimeRatio =
      1 * tests.length - testTimeRatioList.sum
    testTimeRatioList :+= finalTestTimeRatio

    val finalAsSubmissionPreSpan =
      random.nextInt(xSCAidConfiguration.getFinalAsSubmissionWindowSpan)
    val actualExamDuration =
      xSCAidConfiguration.getExamDuration - finalAsSubmissionPreSpan
    val averageTestDuration = actualExamDuration.toDouble / tests.length

    testTimeRatioList.map(ratio => averageTestDuration * ratio * 1000).zipWithIndex.foreach {
      case (testTime, index) =>
        // +1 for answer-sheet submission
        val submissionCount = testAnswersArray(index).length + 1
        val averageSubmitDelay = testTime / submissionCount

        var delaySerial = Range(0, (submissionCount / 2.0).ceil.toInt).flatMap(_ => {
          val delayDeviation = (random.nextDouble() * 2 - 1) *
            xSCAidConfiguration.getSubmissionDelayDeviation

          Array(
            (averageSubmitDelay * (1 - delayDeviation)).toLong,
            (averageSubmitDelay * (1 + delayDeviation)).toLong)
        }).toArray

        if (submissionCount % 2 != 0)
          delaySerial = delaySerial.slice(0, delaySerial.length - 1)

        delaySerial(delaySerial.length - 1) =
          testTime.toLong - delaySerial.slice(0, delaySerial.length - 1).sum

        val shuffledDelaySerial = Random.shuffle(delaySerial.toList)

        testAnswerSubmitDelayArray(index) = shuffledDelaySerial
    }

    assert(testAnswersArray.length == tests.length)
    assert(testAnswerSheetArray.length == tests.length)
    assert(testAnswerSubmitDelayArray.length == tests.length)

    testAnswersArray.zipWithIndex.foreach {
      case (testAnswers, index) =>
        assert(testAnswers.length + 1 == testAnswerSubmitDelayArray(index).length)
    }
  }

  def mock(): Unit = {

    personalExamContext.getTests.asScala.zipWithIndex.foreach {
      case (testInfo, index) =>
        val answerList = testAnswersArray(index)
        val delayList = testAnswerSubmitDelayArray(index)
        val answerSheet = testAnswerSheetArray(index)

        answerList.zipWithIndex.foreach {
          case (answer, answerIndex) =>

            val answerBeginTime = System.currentTimeMillis()
            val answerSubmissionResponse =
              answerService.submitAnswer(testInfo.getTestId,
                personalExamContext.getUserId, answer)
            val answerEndTime = System.currentTimeMillis()

            assert(answerSubmissionResponse.getStatusCode == HttpStatus.OK)

            TimeUnit.MILLISECONDS.sleep(delayList(answerIndex) -
              (answerEndTime - answerBeginTime))
        }

        val asBeginTime = System.currentTimeMillis()
        val answerSheetSubmissionResponse =
          answerSheetService.submitAnswersheet(testInfo.getTicketId, answerSheet)
        val asEndTime = System.currentTimeMillis()

        assert(answerSheetSubmissionResponse.getStatusCode == HttpStatus.OK)

        TimeUnit.MILLISECONDS.sleep(delayList.last -
          (asEndTime - asBeginTime))
    }
  }
}
