package com.beben.tool.openeva.xscaid.service

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.{PersonalExamContext, VCodeLoginData}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod}
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class LoginService(@Autowired private val restTemplate: RestTemplate,
                   @Autowired private val xSCAidConfiguration: XSCAidConfiguration) {

  val requestHeaders = new LinkedMultiValueMap[String, String]()
  requestHeaders.add("X-Auth-Code", xSCAidConfiguration.getXAuthCode)

  def loginByVCode(foreignId: String, vCode: String): PersonalExamContext = {

    val url =
      xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.getLoginPath

    val vCodeLoginData = new VCodeLoginData
    vCodeLoginData.setForeignId(foreignId)
    vCodeLoginData.setVcode(vCode)

    val request = new HttpEntity[VCodeLoginData](vCodeLoginData, requestHeaders)

    restTemplate.exchange(url, HttpMethod.POST, request, classOf[PersonalExamContext])
      .getBody
  }
}
