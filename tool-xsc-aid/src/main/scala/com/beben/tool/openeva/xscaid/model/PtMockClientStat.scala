package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList, Map => JMap}
import java.util.Date

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id

import scala.beans.BeanProperty

@Document
class PtMockClientStat(fid: String, v: String) {

  @Id
  @BeanProperty
  var foreignId: String = fid

  @BeanProperty
  var version: String = v

  @BeanProperty
  var asSubmitStats: JList[SubmitStat] = _

  @BeanProperty
  var answerSubmitStats: JMap[String, JList[SubmitStat]] = _
}

object PtMockClientStat {

  def apply(foreignId: String, version: String): PtMockClientStat =
    new PtMockClientStat(foreignId, version)
}

class SubmitStat(id: String, tt: Date, rs: Int) {

  @BeanProperty
  var identity: String = id

  @BeanProperty
  var triggerTime: Date = tt

  @BeanProperty
  var respSpan: Int = rs
}

object SubmitStat {

  def apply(identity: String, triggerTime: Date, respSpan: Int): SubmitStat =
    new SubmitStat(identity, triggerTime, respSpan)
}
