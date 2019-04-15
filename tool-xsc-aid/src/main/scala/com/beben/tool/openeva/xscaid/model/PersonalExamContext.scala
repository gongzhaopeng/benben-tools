package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList}

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class PersonalExamContext {

  @BeanProperty
  @JsonProperty("begin_time")
  var beginTime: Long = _

  @BeanProperty
  @JsonProperty("cur_server_time")
  var curServerTime: Long = _

  @BeanProperty
  @JsonProperty("duration")
  var duration: Int = _

  @BeanProperty
  @JsonProperty("ongoing_test")
  var ongoingTest: PersonalOngoingTest = _

  @BeanProperty
  @JsonProperty("personal_begin_time")
  var personalBeginTime: Long = _

  @BeanProperty
  @JsonProperty("post_login_duration")
  var postLoginDuration: Int = _

  @BeanProperty
  @JsonProperty("pre_login_duration")
  var preLoginDuration: Int = _

  @BeanProperty
  @JsonProperty("tests")
  var tests: JList[PersonalTestInfo] = _

  @BeanProperty
  @JsonProperty("user_id")
  var userId: String = _
}
