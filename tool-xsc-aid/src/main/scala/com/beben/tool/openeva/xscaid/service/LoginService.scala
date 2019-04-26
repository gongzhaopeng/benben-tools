package com.beben.tool.openeva.xscaid.service

import java.util.{Map => JMap}

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.{PersonalExamContext, TestPaper, VCodeLoginData}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
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

    val vCodeLoginData = VCodeLoginData(foreignId, vCode)

    val request = new HttpEntity[VCodeLoginData](vCodeLoginData, requestHeaders)

    restTemplate.exchange(url, HttpMethod.POST, request, classOf[PersonalExamContext])
      .getBody
  }

  def fetchPapers(userId: String): JMap[String, TestPaper] = {

    val url = xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.getPaperPath

    val request = new HttpEntity[Void](null, requestHeaders)

    restTemplate.exchange(url, HttpMethod.GET, request,
      new ParameterizedTypeReference[JMap[String, TestPaper]] {}, userId).getBody
  }
}
