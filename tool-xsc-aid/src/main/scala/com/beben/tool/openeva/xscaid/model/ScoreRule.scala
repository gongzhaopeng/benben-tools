package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class ScoreRule {

  @BeanProperty
  @JsonProperty("description")
  var description: String = _

  @BeanProperty
  @JsonProperty("id")
  var id: String = _

  @BeanProperty
  @JsonProperty("score")
  var score: Int = _
}
