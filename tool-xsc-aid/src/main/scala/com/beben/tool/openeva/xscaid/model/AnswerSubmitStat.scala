package com.beben.tool.openeva.xscaid.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import scala.beans.BeanProperty

@Document
class AnswerSubmitStat(_id: String,
                       _version: String,
                       _foreignId: String,
                       _questionId: String,
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
  var questionId: String = _questionId

  @BeanProperty
  var triggerTime: Long = _triggerTime

  @BeanProperty
  var respSpan: Int = _respSpan
}

object AnswerSubmitStat {

  def apply(version: String,
            foreignId: String,
            questionId: String,
            triggerTime: Long,
            respSpan: Int): AnswerSubmitStat =
    new AnswerSubmitStat(null, version, foreignId, questionId, triggerTime, respSpan)
}
