package com.beben.tool.openeva.xscaid.schedule

import com.beben.tool.openeva.xscaid.pressuretest.ClientMockMachine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PressureTestScheduler(@Autowired private val clientMockMachine: ClientMockMachine) {

  @Scheduled(cron = """${xsc.aid.pt-schedule-cron}""")
  def launchClientMockMachine(): Unit = {

    clientMockMachine.launch()
  }
}
