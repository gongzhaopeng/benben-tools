package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList}

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class TestPaper {

  @BeanProperty
  @JsonProperty("description")
  var description: String = _

  @BeanProperty
  @JsonProperty("id")
  var id: String = _

  @BeanProperty
  @JsonProperty("question_sets")
  var questionSets: JList[QuestionSet] = _

  @BeanProperty
  @JsonProperty("scoreRule")
  var scoreRule: ScoreRule = _
}
