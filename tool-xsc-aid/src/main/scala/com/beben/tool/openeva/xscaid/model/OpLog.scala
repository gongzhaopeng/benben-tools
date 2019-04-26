package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class OpLog(et: Long, m: String, st: Long) {

  def this() = this(0, null, 0)

  @BeanProperty
  @JsonProperty("end_time")
  var endTime: Long = et

  @BeanProperty
  @JsonProperty("misc")
  var misc: String = m

  @BeanProperty
  @JsonProperty("start_time")
  var startTime: Long = st
}

object OpLog {

  def apply(endTime: Long, misc: String, startTime: Long): OpLog =
    new OpLog(endTime, misc, startTime)
}
