package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class ChoiceResult(c: String, s: String) {

  def this() = this(null, null)

  @BeanProperty
  @JsonProperty("content")
  var content: String = c

  @BeanProperty
  @JsonProperty("style")
  var style: String = s
}

object ChoiceResult {

  def apply(content: String, style: String): ChoiceResult =
    new ChoiceResult(content, style)
}
