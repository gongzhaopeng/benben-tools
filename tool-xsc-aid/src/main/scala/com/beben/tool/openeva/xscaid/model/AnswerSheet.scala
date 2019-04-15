package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList}

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class AnswerSheet {

  @BeanProperty
  @JsonProperty("answers")
  var answers: JList[Answer] = _

  @BeanProperty
  @JsonProperty("id")
  var id: String = _

  @BeanProperty
  @JsonProperty("oplog")
  var oplog: OpLog = _

  @BeanProperty
  @JsonProperty("ref_AdmissionTicket")
  var refAAdmissionTicket: String = _
}
