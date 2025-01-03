package com.eff3ct.teckel.interface

import com.eff3ct.teckel.serializer.model.etl.ETL

object request {

  case class TeckelRequest(jobConfigRequest: SparkJobConfigRequest, etl: ETL)
  case class SparkJobConfigRequest(url: String)
}
