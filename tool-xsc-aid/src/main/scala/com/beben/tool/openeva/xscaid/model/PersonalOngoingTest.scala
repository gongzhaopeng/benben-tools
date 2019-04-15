package com.beben.tool.openeva.xscaid.model

import java.util.{Map => JMap}

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class PersonalOngoingTest {

  @BeanProperty
  @JsonProperty("commited_answers")
  var commitedAnswers: JMap[String, Answer] = _

  @BeanProperty
  @JsonProperty("test_id")
  var testId: String = _
}
