package com.beben.tool.openeva.xscaid.repository

import com.beben.tool.openeva.xscaid.model.PtMockClientStat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
trait PtMockClientStatRepository extends MongoRepository[PtMockClientStat, String] {

}
