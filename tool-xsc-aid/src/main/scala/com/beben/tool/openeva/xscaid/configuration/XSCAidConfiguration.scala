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
  var testIds: JList[String] = _
}
