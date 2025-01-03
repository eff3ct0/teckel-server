/*
 * MIT License
 *
 * Copyright (c) 2024 Rafael Fernandez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
