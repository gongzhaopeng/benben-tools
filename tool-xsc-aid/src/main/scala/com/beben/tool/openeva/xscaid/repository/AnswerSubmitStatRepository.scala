package com.beben.tool.openeva.xscaid.repository

import com.beben.tool.openeva.xscaid.model.AnswerSubmitStat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
trait AnswerSubmitStatRepository
  extends MongoRepository[AnswerSubmitStat, String] {

}
