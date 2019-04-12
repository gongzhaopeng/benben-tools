package com.beben.tool.openeva.xscaid

import com.beben.tool.openeva.xscaid.model.ForeignUser
import com.beben.tool.openeva.xscaid.service.TicketService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication;

@SpringBootApplication
class AccountsGenApplication {

}

object AccountsGenApplication extends App {

  val applicationContext =
    SpringApplication.run(classOf[AccountsGenApplication], args: _*)

  val ticketService = applicationContext.getBean(classOf[TicketService])

  val identityToPhoneMap = Map(
    "53242819851018263X" -> "18612447216")

  val fUsers = identityToPhoneMap.map({
    case (identity, phone) =>
      val foreignUser = new ForeignUser
      foreignUser.setForeignId(identity)
      foreignUser.setPhone(phone)

      foreignUser
  }).toList

  ticketService.generateTickets(fUsers)

  SpringApplication.exit(applicationContext)
}
