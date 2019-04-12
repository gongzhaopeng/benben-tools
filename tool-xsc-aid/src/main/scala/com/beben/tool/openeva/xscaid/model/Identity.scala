package com.beben.tool.openeva.xscaid.model

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class Identity {

  @BeanProperty
  @JsonProperty("birthday")
  var birthday: String = _

  @BeanProperty
  @JsonProperty("first_name")
  var firstName: String = _

  @BeanProperty
  @JsonProperty("id_number")
  var idNumber: String = _

  @BeanProperty
  @JsonProperty("last_name")
  var lastName: String = _

  @BeanProperty
  @JsonProperty("region")
  var region: String = _

  @BeanProperty
  @JsonProperty("sex")
  var sex: String = _
}
