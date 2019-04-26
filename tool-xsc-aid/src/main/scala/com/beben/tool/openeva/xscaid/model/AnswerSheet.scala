package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList}

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class AnswerSheet(as: JList[Answer], i: String, ol: OpLog, rat: String) {

  def this() = this(null, null, null, null)

  @BeanProperty
  @JsonProperty("answers")
  var answers: JList[Answer] = as

  @BeanProperty
  @JsonProperty("id")
  var id: String = i

  @BeanProperty
  @JsonProperty("oplog")
  var oplog: OpLog = ol

  @BeanProperty
  @JsonProperty("ref_AdmissionTicket")
  var refAAdmissionTicket: String = rat
}

object AnswerSheet {

  def apply(answers: JList[Answer], id: String, oplog: OpLog,
            refAAdmissionTicket: String): AnswerSheet =
    new AnswerSheet(answers, id, oplog, refAAdmissionTicket)
}


