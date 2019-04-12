package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class Ticket {

  @BeanProperty
  @JsonProperty("id")
  var id: String = _

  @BeanProperty
  @JsonProperty("test")
  var test: String = _

  @BeanProperty
  @JsonProperty("testpaper")
  var testpaper: String = _

  @BeanProperty
  @JsonProperty("user")
  var user: String = _
}
