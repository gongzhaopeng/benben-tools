package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class VCodeLoginData {

  @BeanProperty
  @JsonProperty("foreign_id")
  var foreignId: String = _

  @BeanProperty
  @JsonProperty("vcode")
  var vcode: String = _
}
