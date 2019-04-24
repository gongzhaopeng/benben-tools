package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class PersonalTestInfo {

  @BeanProperty
  @JsonProperty("description")
  var description: String = _

  @BeanProperty
  @JsonProperty("end_time")
  var endTime: Long = _

  @BeanProperty
  @JsonProperty("start_time")
  var startTime: Long = _

  @BeanProperty
  @JsonProperty("style")
  var style: String = _

  @BeanProperty
  @JsonProperty("test_id")
  var testId: String = _

  @BeanProperty
  @JsonProperty("ticket_id")
  var ticketId: String = _
}
