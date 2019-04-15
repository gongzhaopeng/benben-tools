package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList}

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class QuestionSet {

  @BeanProperty
  @JsonProperty("description")
  var description: String = _

  @BeanProperty
  @JsonProperty("id")
  var id: String = _

  @BeanProperty
  @JsonProperty("limit_time")
  var limit_time: Int = _

  @BeanProperty
  @JsonProperty("questions")
  var questions: JList[Question] = _

  @BeanProperty
  @JsonProperty("scoreRule")
  var scoreRule: ScoreRule = _
}
