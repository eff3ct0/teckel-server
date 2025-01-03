package com.eff3ct.teckel.service

import com.eff3ct.teckel.api.core.Run
import com.eff3ct.teckel.api.data.etl
import com.eff3ct.teckel.semantic.execution._
import com.eff3ct.teckel.serializer.model.etl.ETL
import org.apache.spark.sql.SparkSession

trait TeckelService[F[_]] {

  def execute(data: ETL): F[Unit]

}

object TeckelService {

  def apply[F[_]: TeckelService]: TeckelService[F] =
    implicitly[TeckelService[F]]

  def impl[F[_]: Run](S: SparkSession): TeckelService[F] = {
    implicit val spark: SparkSession = S
    (data: ETL) => etl[F, Unit](data)
  }

}
