package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList, Map => JMap}
import java.util.Date

import com.beben.tool.openeva.xscaid.model.PtMockClientStat.CompositeKey
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id

import scala.beans.BeanProperty

@Document
class PtMockClientStat(i: CompositeKey) {

  @Id
  @BeanProperty
  var id: CompositeKey = i

  @BeanProperty
  var asSubmitStats: JList[SubmitStat] = _

  @BeanProperty
  var answerSubmitStats: JMap[String, JList[SubmitStat]] = _
}

object PtMockClientStat {

  def apply(id: CompositeKey): PtMockClientStat =
    new PtMockClientStat(id)

  class CompositeKey(fid: String, v: String) {

    @BeanProperty
    var foreignId: String = fid

    @BeanProperty
    var version: String = v
  }

  object CompositeKey {

    def apply(foreignId: String, version: String): CompositeKey =
      new CompositeKey(foreignId, version)
  }

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
