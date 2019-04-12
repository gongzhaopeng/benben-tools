package com.beben.tool.openeva.xscaid

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PressureTestApplication {

}

object PressureTestApplication extends App {

  val applicationContext =
    SpringApplication.run(classOf[PressureTestTicketsGenApplication], args: _*)
}
