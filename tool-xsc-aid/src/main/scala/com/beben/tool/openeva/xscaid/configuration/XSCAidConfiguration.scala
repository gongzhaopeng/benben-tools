package com.beben.tool.openeva.xscaid.configuration

import java.util.{List => JList}

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

@Component
@ConfigurationProperties("xsc.aid")
class XSCAidConfiguration {

  @BeanProperty
  var xAuthCode: String = _

  @BeanProperty
  var serverBaseUrl: String = _

  @BeanProperty
  var ticketGenPath: String = _

  @BeanProperty
  var fidToUidPath: String = _

  @BeanProperty
  var ticketsByUidPath: String = _

  @BeanProperty
  var singleTestPath: String = _

  @BeanProperty
  var loginPath: String = _

  @BeanProperty
  var paperPath: String = _

  @BeanProperty
  var answerPostPath: String = _

  @BeanProperty
  var answersheetPostPath: String = _

  @BeanProperty
  var testIds: JList[String] = _

  @BeanProperty
  var pressureForeignIdPrefix: String = _

  @BeanProperty
  var pressureUserCount: Int = _

  @BeanProperty
  var ptLeadingUserIndex: Int = _

  @BeanProperty
  var ptUserCount: Int = _

  @BeanProperty
  var answerSubmissionRepeat: Double = _

  @BeanProperty
  var godVcode: String = _

  @BeanProperty
  var examDuration: Int = _

  @BeanProperty
  var mockClientTriggerWindowSpan: Int = _

  @BeanProperty
  var finalAsSubmissionWindowSpan: Int = _

  @BeanProperty
  var testTimeDeviation: Double = _

  @BeanProperty
  var userLevelCount: Int = _

  @BeanProperty
  var answersCountRatioGap: Double = _

  @BeanProperty
  var submissionDelayDeviation: Double = _
}
