package com.beben.tool.openeva.xscaid.service

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.Answer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod, ResponseEntity}
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class AnswerService(@Autowired private val restTemplate: RestTemplate,
                    @Autowired private val xSCAidConfiguration: XSCAidConfiguration) {

  val requestHeaders = new LinkedMultiValueMap[String, String]()
  requestHeaders.add("X-Auth-Code", xSCAidConfiguration.getXAuthCode)

  def submitAnswer(testId: String, userId: String, answer: Answer): ResponseEntity[Void] = {

    val url =
      xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.answerPostPath

    val request = new HttpEntity[Answer](answer, requestHeaders)

    restTemplate.exchange(url, HttpMethod.POST, request, classOf[Void], testId, userId)
  }
}
