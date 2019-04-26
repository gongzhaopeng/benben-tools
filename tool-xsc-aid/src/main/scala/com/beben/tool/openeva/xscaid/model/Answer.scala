package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class Answer(ol: OpLog, rq: String, r: ChoiceResult) {

  def this() = this(null, null, null)

  @BeanProperty
  @JsonProperty("oplog")
  var oplog: OpLog = ol

  @BeanProperty
  @JsonProperty("ref_question")
  var refQuestion: String = rq

  @BeanProperty
  @JsonProperty("result")
  var result: ChoiceResult = r
}

object Answer {

  def apply(oplog: OpLog, refQuestion: String, result: ChoiceResult): Answer =
    new Answer(oplog, refQuestion, result)
}
