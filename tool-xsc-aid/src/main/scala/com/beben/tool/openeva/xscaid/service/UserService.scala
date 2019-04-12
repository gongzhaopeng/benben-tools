package com.beben.tool.openeva.xscaid.service

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.ForeignUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod}
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class UserService(@Autowired private val restTemplate: RestTemplate,
                  @Autowired private val xSCAidConfiguration: XSCAidConfiguration) {

  val requestHeaders = new LinkedMultiValueMap[String, String]()
  requestHeaders.add("X-Auth-Code", xSCAidConfiguration.getXAuthCode)

  def queryUidByFid(fid: String): String = {

    val url =
      xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.getFidToUidPath
    val request = new HttpEntity[ForeignUser](null, requestHeaders)

    restTemplate.exchange(url, HttpMethod.GET, request, classOf[String], fid).getBody
  }
}
