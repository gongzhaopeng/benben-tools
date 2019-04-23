package com.beben.tool.openeva.xscaid.pressuretest

import java.util.concurrent.{ExecutorService, Executors}

import com.beben.tool.openeva.xscaid.configuration.XSCAidConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import org.springframework.stereotype.Component

import scala.util.Random

@Component
class ClientMockMachine(@Autowired private val xSCAidConfiguration: XSCAidConfiguration)
  extends ApplicationContextAware {

  private val log = LoggerFactory.getLogger(classOf[ClientMockMachine])

  private var applicationContext: ApplicationContext = _

  private var clientMockers: List[ClientMocker] = _

  private var executorService: ExecutorService = _

  override def setApplicationContext(applicationContext: ApplicationContext): Unit = {
    this.applicationContext = applicationContext
  }

  def init(): Unit = {

    val random = new Random()

    clientMockers = Range(xSCAidConfiguration.getPtLeadingUserIndex,
      xSCAidConfiguration.getPtUserCount).map(i => {

      val foreignId = xSCAidConfiguration.getPressureForeignIdPrefix + i

      val clientMocker = applicationContext.getBean(classOf[ClientMocker])

      clientMocker.init(
        random.nextInt(xSCAidConfiguration.getMockClientTriggerWindowSpan * 1000),
        foreignId)

      clientMocker
    }).toList

    executorService = Executors.newFixedThreadPool(
      xSCAidConfiguration.getPressureUserCount)
  }


  def launch(): Unit = {

    clientMockers.foreach(clientMocker => {

      executorService.execute(() => {

        log.info("Mock-Client[Foreign-ID=>{}] begins", clientMocker.foreignId)

        val startTime = System.currentTimeMillis

        clientMocker.mock()

        val endTime = System.currentTimeMillis

        log.info("Foreign-ID: {}, Time-Elapsed: {}", clientMocker.foreignId, endTime - startTime)
      })

    })

    //            executorService.shutdown()
  }
}
