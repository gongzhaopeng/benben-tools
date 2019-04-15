package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class Answer {

  @BeanProperty
  @JsonProperty("oplog")
  var oplog: OpLog = _

  @BeanProperty
  @JsonProperty("ref_question")
  var refQuestion: String = _

  @BeanProperty
  @JsonProperty("result")
  var result: ChoiceResult = _
}
