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
    "53242819851018152X" -> "18612447216")

  val testIds = List(
    "fad153df-a125-452d-a726-ee18d5142279",
    "21dc7f9d-80a7-41da-8759-d49f936501ff",
    "62ab7ec7-3edf-481a-aec3-352fbe145681",
    "6cf31943-4d59-4fde-9781-ad0aa0b72ec6",
    "7d6d3cac-1235-4d2b-98cd-a508d5ad7d08")

  identityToPhoneMap.foreach(identityToPhone => {

    val foreignUser = new ForeignUser
    foreignUser.setForeignId(identityToPhone._1)
    foreignUser.setPhone(identityToPhone._2)

    testIds.foreach(testId =>
      ticketService.generateTicketByTidAndForeignUser(
        testId, foreignUser))
  })
}
