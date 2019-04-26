package com.beben.tool.openeva.xscaid.pressuretest

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit
import java.util.{List => JList, ArrayList => JArrayList}

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
  private var testPapers: Map[String, TestPaper] = _

  // unit=>ms
  var triggerDelay: Int = _

  var foreignId: String = _

  private var userLevel: Int = _

  private var testAnswersArray: Array[List[Answer]] = _
  private var testAnswerSheetArray: Array[AnswerSheet] = _
  private var testAnswerSubmitDelayArray: Array[List[Long]] = _

  private var ptMockClientStat: PtMockClientStat = _

  def getPtMockClientStat: PtMockClientStat = ptMockClientStat

  def init(triggerDelay: Int, foreignId: String): Unit = {

    this.foreignId = foreignId

    val random = new Random()

    userLevel = random.nextInt(xSCAidConfiguration.getUserLevelCount)

    personalExamContext =
      loginService.loginByVCode(foreignId, xSCAidConfiguration.getGodVcode)
    testPapers = loginService.fetchPapers(personalExamContext.getUserId).asScala.toMap
    assert(personalExamContext.getTests.size() == testPapers.size)

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

        val answers = testPapers(testInfo.getTestId).getQuestionSets.asScala.flatMap(
          _.getQuestions.asScala).map(question => {

          Answer(
            OpLog(curTime, "Mocked misc.", curTime),
            question.getId,
            ChoiceResult("Mocked content.", "Mocked style."))
        })

        testAnswersArray(index) =
          Range(0, actualAnswerSubmissionRepeat.ceil.toInt)
            .map(_ => answers).reduce(_ ++ _)
            .slice(0, (answers.size * actualAnswerSubmissionRepeat).ceil.toInt)
            .toList

        testAnswerSheetArray(index) = AnswerSheet(
          answers.asJava,
          null,
          OpLog(curTime, "Mocked misc.", curTime),
          testInfo.getTestId)
    }

    var testTimeRatioList = Range(0, tests.length - 1).map(_ =>
      1 - (random.nextDouble() * 2 - 1) * xSCAidConfiguration.getTestTimeDeviation)
    val finalTestTimeRatio = 1 * tests.length - testTimeRatioList.sum
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

          List(
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

    ptMockClientStat = PtMockClientStat(
      foreignId,
      new SimpleDateFormat("yyyy-MM-dd HH").format(new Date))
    ptMockClientStat.setAsSubmitStats(new JArrayList[SubmitStat]())
    ptMockClientStat.setAnswerSubmitStats(
      tests.map(_.testId ->
        (new JArrayList[SubmitStat](): JList[SubmitStat])).toMap.asJava
    )
  }

  def mock(): Unit = {

    TimeUnit.MILLISECONDS.sleep(triggerDelay)

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
            val answerRespSpan = answerEndTime - answerBeginTime

            val answerSubmitStat = SubmitStat(
              answer.getRefQuestion,
              new Date(answerBeginTime),
              answerRespSpan.toInt)
            ptMockClientStat.getAnswerSubmitStats.get(testInfo.testId)
              .add(answerSubmitStat)

            assert(answerSubmissionResponse.getStatusCode == HttpStatus.OK)

            TimeUnit.MILLISECONDS.sleep(delayList(answerIndex) - answerRespSpan)
        }

        val asBeginTime = System.currentTimeMillis()
        val answerSheetSubmissionResponse =
          answerSheetService.submitAnswersheet(testInfo.getTicketId, answerSheet)
        val asEndTime = System.currentTimeMillis()
        val asRespSpan = asEndTime - asBeginTime

        val asSubmitStat = SubmitStat(
          testInfo.testId,
          new Date(asBeginTime),
          asRespSpan.toInt)
        ptMockClientStat.getAsSubmitStats.add(asSubmitStat)

        assert(answerSheetSubmissionResponse.getStatusCode == HttpStatus.OK)

        TimeUnit.MILLISECONDS.sleep(delayList.last - asRespSpan)
    }
  }
}
