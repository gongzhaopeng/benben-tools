package com.beben.tool.openeva.xscaid.model

import java.util.{List => JList}

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class Content {

  @BeanProperty
  @JsonProperty("pics")
  var pics: JList[ContPic] = _

  @BeanProperty
  @JsonProperty("txts")
  var txts: JList[ContTxt] = _
}
