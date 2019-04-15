package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class ChoiceResult {

  @BeanProperty
  @JsonProperty("content")
  var content: String = _

  @BeanProperty
  @JsonProperty("style")
  var style: String = _
}
