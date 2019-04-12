package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class ForeignUser {

  @BeanProperty
  @JsonProperty("foreign_id")
  var foreignId: String = _

  @BeanProperty
  @JsonProperty("identity")
  var identity: Identity = _

  @BeanProperty
  @JsonProperty("phone")
  var phone: String = _
}
