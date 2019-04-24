package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList, Map => JMap}

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id

import scala.beans.BeanProperty

@Document
class PtMockClientStat {

  @Id
  @BeanProperty
  var foreignId: String = _

  @BeanProperty
  var version: String = _

  @BeanProperty
  var asSubmitStats: JList[SubmitStat] = _

  @BeanProperty
  var answerSubmitStats: JMap[String, JList[SubmitStat]] = _
}

class SubmitStat {

  @BeanProperty
  var id: String = _

  @BeanProperty
  var triggerTimestamp: Long = _

  @BeanProperty
  var respSpan: Int = _
}
