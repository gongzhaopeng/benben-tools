package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class Stem {

  @BeanProperty
  @JsonProperty("content")
  var content: Content = _

  @BeanProperty
  @JsonProperty("style")
  var style: String = _
}
