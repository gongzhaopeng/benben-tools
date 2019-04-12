package com.beben.tool.openeva.xscaid.service

import java.util.{List => JList}

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.ForeignUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.{HttpEntity, HttpMethod}
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import com.beben.tool.openeva.xscaid.model.Ticket
import org.slf4j.LoggerFactory

@Service
class TicketService(@Autowired private val restTemplate: RestTemplate,
                    @Autowired private val xSCAidConfiguration: XSCAidConfiguration,
                    @Autowired private val userService: UserService) {

  private val log = LoggerFactory.getLogger(classOf[TicketService])

  private val requestHeaders = new LinkedMultiValueMap[String, String]()
  requestHeaders.add("X-Auth-Code", xSCAidConfiguration.getXAuthCode)

  import scala.collection.JavaConverters._

  private val testIds = xSCAidConfiguration.getTestIds.asScala

  def generateTickets(fUsers: List[ForeignUser]): Unit = {

    import scala.collection.JavaConverters._

    fUsers.foreach(fUser => {

      val currentTickets = fetchTicketsByFid(fUser.getForeignId).asScala

      testIds.foreach(testId =>
        if (currentTickets.exists(_.getTest == testId))
          log.warn("Ticket for ForeignId: {}, TestId: {} exists",
            List(fUser.getForeignId, testId): _*)
        else
          generateTicketByTidAndForeignUser(testId, fUser)
      )
    })
  }

  def fetchTicketsByFid(fid: String): JList[Ticket] = {

    val uid = userService.queryUidByFid(fid)

    if (uid == null) JList.of()
    else {
      val url =
        xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.getTicketsByUidPath
      val request = new HttpEntity[ForeignUser](null, requestHeaders)

      val tickets = restTemplate.exchange(url, HttpMethod.GET, request,
        new ParameterizedTypeReference[JList[Ticket]] {}, uid).getBody
      if (tickets == null) JList.of() else tickets
    }
  }

  private def generateTicketByTidAndForeignUser(testId: String,
                                                foreignUser: ForeignUser): Unit = {
    val url =
      xSCAidConfiguration.getServerBaseUrl + xSCAidConfiguration.getTicketGenPath
    val request = new HttpEntity[ForeignUser](foreignUser, requestHeaders)

    restTemplate.postForEntity(url, request, classOf[String], testId)
  }
}
