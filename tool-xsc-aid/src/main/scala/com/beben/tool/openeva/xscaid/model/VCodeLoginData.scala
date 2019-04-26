package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class VCodeLoginData(fid: String, vc: String) {

  @BeanProperty
  @JsonProperty("foreign_id")
  var foreignId: String = fid

  @BeanProperty
  @JsonProperty("vcode")
  var vcode: String = vc
}

object VCodeLoginData {

  def apply(foreignId: String, vcode: String): VCodeLoginData =
    new VCodeLoginData(foreignId, vcode)
}