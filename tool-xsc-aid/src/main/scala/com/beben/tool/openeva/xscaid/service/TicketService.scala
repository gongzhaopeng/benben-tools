package com.beben.tool.openeva.xscaid.service

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.ForeignUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class TicketService(@Autowired private val restTemplate: RestTemplate,
                    @Autowired private val xSCAidConfiguration: XSCAidConfiguration) {

  val requestHeaders = new LinkedMultiValueMap[String, String]()
  requestHeaders.add("X-Auth-Code", xSCAidConfiguration.getXAuthCode)

  def generateTicketByTidAndForeignUser(testId: String,
                                        foreignUser: ForeignUser): Unit = {
    val url =
      xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.getTicketGenPath
    val request = new HttpEntity[ForeignUser](foreignUser, requestHeaders)

    restTemplate.postForEntity(url, request, classOf[String], testId)
  }
}
