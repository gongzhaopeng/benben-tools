package com.beben.tool.openeva.xscaid

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import com.beben.tool.openeva.xscaid.model.ForeignUser
import com.beben.tool.openeva.xscaid.service.TicketService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PressureTestTicketsGenApplication {

}

object PressureTestTicketsGenApplication extends App {

  val applicationContext =
    SpringApplication.run(classOf[PressureTestTicketsGenApplication], args: _*)

  val ticketService = applicationContext.getBean(classOf[TicketService])

  val xSCAidConfiguration = applicationContext.getBean(classOf[XSCAidConfiguration])

  val foreignUsers = Range(0, xSCAidConfiguration.getPressureUserCount).map(i => {

    val foreignId = xSCAidConfiguration.getPressureForeignIdPrefix + i

    val foreignUser = new ForeignUser
    foreignUser.setForeignId(foreignId)
    foreignUser.setPhone("18612447216")

    foreignUser
  }).toList

  ticketService.generateTickets(foreignUsers)

  SpringApplication.exit(applicationContext)
}
