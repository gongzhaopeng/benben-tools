package com.beben.tool.openeva.xscaid.configuration

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {

  @Bean
  def restTemplate(): RestTemplate = new RestTemplate()
}