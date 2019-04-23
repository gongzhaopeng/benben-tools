package com.beben.tool.openeva.xscaid

import com.beben.tool.openeva.xscaid.pressuretest.ClientMockMachine
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PressureTestApplication {

}

object PressureTestApplication extends App {

  val applicationContext =
    SpringApplication.run(classOf[PressureTestApplication], args: _*)

  val clientMockMachine = applicationContext.getBean(classOf[ClientMockMachine])
  clientMockMachine.init()

  //  SpringApplication.exit(applicationContext)
}
