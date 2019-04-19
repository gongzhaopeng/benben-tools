package com.beben.tool.openeva.xscaid

import com.beben.tool.openeva.xscaid.model.ForeignUser
import com.beben.tool.openeva.xscaid.service.TicketService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication;

@SpringBootApplication
class TicketGenApplication {

}

object TicketGenApplication extends App {

  val applicationContext =
    SpringApplication.run(classOf[TicketGenApplication], args: _*)

  val ticketService = applicationContext.getBean(classOf[TicketService])

  val identityToPhoneMap = Map(
    "53242819851018041X" -> "18612447216",  // 龚兆鹏
    "130731199308031513" -> "13717650684",  // 刘进轩
    "140602199310200000" -> "17600208132")  // 唐天星

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
