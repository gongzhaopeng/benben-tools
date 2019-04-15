package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class Question {

  @BeanProperty
  @JsonProperty("choices")
  var choices: Choices = _

  @BeanProperty
  @JsonProperty("description")
  var description: String = _

  @BeanProperty
  @JsonProperty("dimension")
  var dimension: String = _

  @BeanProperty
  @JsonProperty("id")
  var id: String = _

  @BeanProperty
  @JsonProperty("stem")
  var stem: Stem = _

  @BeanProperty
  @JsonProperty("style")
  var style: String = _
}
