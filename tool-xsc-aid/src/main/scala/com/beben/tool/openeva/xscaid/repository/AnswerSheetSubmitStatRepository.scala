package com.beben.tool.openeva.xscaid.repository

import com.beben.tool.openeva.xscaid.model.AnswerSheetSubmitStat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
trait AnswerSheetSubmitStatRepository
  extends MongoRepository[AnswerSheetSubmitStat, String] {

}
