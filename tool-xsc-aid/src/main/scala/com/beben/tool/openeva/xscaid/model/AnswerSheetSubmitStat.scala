package com.beben.tool.openeva.xscaid.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import scala.beans.BeanProperty

@Document
class AnswerSheetSubmitStat(_id: String,
                            _version: String,
                            _foreignId: String,
                            _testId: String,
                            _triggerTime: Long,
                            _respSpan: Int) {

  @Id
  @BeanProperty
  var id: String = _id

  @BeanProperty
  var version: String = _version

  @BeanProperty
  var foreignId: String = _foreignId

  @BeanProperty
  var testId: String = _testId

  @BeanProperty
  var triggerTime: Long = _triggerTime

  @BeanProperty
  var respSpan: Int = _respSpan
}

object AnswerSheetSubmitStat {

  def apply(version: String,
            foreignId: String,
            testId: String,
            triggerTime: Long,
            respSpan: Int): AnswerSheetSubmitStat =
    new AnswerSheetSubmitStat(null, version, foreignId, testId, triggerTime, respSpan)
}
