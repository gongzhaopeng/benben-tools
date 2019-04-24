package com.beben.tool.openeva.xscaid

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

  //  SpringApplication.exit(applicationContext)
}
