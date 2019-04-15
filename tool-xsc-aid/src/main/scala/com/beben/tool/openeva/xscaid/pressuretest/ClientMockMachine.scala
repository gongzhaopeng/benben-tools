package com.beben.tool.openeva.xscaid.pressuretest

import java.util.concurrent.Executors

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import org.springframework.stereotype.Component

@Component
class ClientMockMachine(@Autowired private val xSCAidConfiguration: XSCAidConfiguration)
  extends ApplicationContextAware {

  private val log = LoggerFactory.getLogger(classOf[ClientMockMachine])

  private var applicationContext: ApplicationContext = _

  override def setApplicationContext(applicationContext: ApplicationContext): Unit = {
    this.applicationContext = applicationContext
  }

  def launch(): Unit = {

    val executorService = Executors.newFixedThreadPool(
      xSCAidConfiguration.getPressureUserCount)

    Range(xSCAidConfiguration.getPtLeadingUserIndex,
      xSCAidConfiguration.getPtUserCount).foreach(i => {

      val foreignId = xSCAidConfiguration.getPressureForeignIdPrefix + i

      executorService.execute(() => {

        val startTime = System.currentTimeMillis

        val clientMocker = applicationContext.getBean(classOf[ClientMocker])

        clientMocker.mock(foreignId)

        val endTime = System.currentTimeMillis

        log.info("Foreign-ID: {}, Time-Elapsed: {}", foreignId, endTime - startTime)
      })

    })

    //            executorService.shutdown()
  }
}
