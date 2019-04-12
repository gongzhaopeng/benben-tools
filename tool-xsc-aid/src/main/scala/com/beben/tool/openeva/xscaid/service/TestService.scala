package com.beben.tool.openeva.xscaid.service

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.ForeignUser
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod, HttpStatus}
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class TestService(@Autowired private val restTemplate: RestTemplate,
                  @Autowired private val xSCAidConfiguration: XSCAidConfiguration)
  extends InitializingBean {

  private val requestHeaders = new LinkedMultiValueMap[String, String]()
  requestHeaders.add("X-Auth-Code", xSCAidConfiguration.getXAuthCode)

  def afterPropertiesSet() {

    import scala.collection.JavaConverters._

    assert(xSCAidConfiguration.getTestIds.asScala.forall(testExist))
  }

  def testExist(testId: String): Boolean = {

    val url =
      xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.getSingleTestPath
    val request = new HttpEntity[ForeignUser](null, requestHeaders)

    restTemplate.exchange(url, HttpMethod.GET, request, classOf[String], testId)
      .getStatusCode.equals(HttpStatus.OK)
  }
}
