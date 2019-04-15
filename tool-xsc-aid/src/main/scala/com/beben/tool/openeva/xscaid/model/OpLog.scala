package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class OpLog {

  @BeanProperty
  @JsonProperty("end_time")
  var endTime: Long = _

  @BeanProperty
  @JsonProperty("misc")
  var misc: String = _

  @BeanProperty
  @JsonProperty("start_time")
  var startTime: Long = _
}
