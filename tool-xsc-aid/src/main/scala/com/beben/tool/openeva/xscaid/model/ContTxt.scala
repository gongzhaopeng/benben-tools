package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class ContTxt {

  @BeanProperty
  @JsonProperty("name")
  var name: String = _

  @BeanProperty
  @JsonProperty("value")
  var value: String = _
}
