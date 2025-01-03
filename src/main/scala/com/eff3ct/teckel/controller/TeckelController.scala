package com.eff3ct.teckel.controller

import cats.effect.Async
import cats.implicits._
import com.eff3ct.teckel.api.core.Run
import com.eff3ct.teckel.interface.request._
import com.eff3ct.teckel.service.TeckelService
import io.circe.generic.auto._
import org.apache.spark.sql.SparkSession
import org.http4s.Status.Ok
import org.http4s.circe.CirceEntityCodec._
import org.http4s.{Request, Response}
import org.typelevel.log4cats.Logger

object TeckelController {

  def execute[F[_]: Async: Run: Logger](request: Request[F]): F[Response[F]] = {
    for {
      teckelReq <- request.as[TeckelRequest]
      spark = SparkSession.builder().remote(teckelReq.jobConfigRequest.url).getOrCreate()
      etl   = teckelReq.etl
      _ <- Logger[F].info(etl.toString)
      _ <- TeckelService.impl[F](spark).execute(etl)
    } yield Response[F](status = Ok)

  }

}
