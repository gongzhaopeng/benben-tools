package com.beben.tool.openeva.xscaid.service

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod, ResponseEntity}
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import com.beben.tool.openeva.xscaid.model.AnswerSheet

@Service
class AnswersheetService(@Autowired private val restTemplate: RestTemplate,
                         @Autowired private val xSCAidConfiguration: XSCAidConfiguration) {

  val requestHeaders = new LinkedMultiValueMap[String, String]()
  requestHeaders.add("X-Auth-Code", xSCAidConfiguration.getXAuthCode)
  requestHeaders.add("Accept", "application/json")
  requestHeaders.add("Content-Type", "application/json")

  def submitAnswersheet(ticketId: String,
                        answerSheet: AnswerSheet): ResponseEntity[AnswerSheet] = {

    val url =
      xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.answersheetPostPath

    val request = new HttpEntity[AnswerSheet](answerSheet, requestHeaders)

    restTemplate.exchange(url, HttpMethod.POST, request, classOf[AnswerSheet], ticketId)
  }
}
