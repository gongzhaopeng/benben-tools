package com.beben.tool.openeva.xscaid

import com.beben.tool.openeva.xscaid.pressuretest.ClientMockMachine
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PressureTestApplication {

}

object PressureTestApplication extends App {

  val applicationContext =
    SpringApplication.run(classOf[PressureTestApplication], args: _*)

  val clientMockMachine = applicationContext.getBean(classOf[ClientMockMachine])
  clientMockMachine.init()
  clientMockMachine.launch()

  //  SpringApplication.exit(applicationContext)
}
