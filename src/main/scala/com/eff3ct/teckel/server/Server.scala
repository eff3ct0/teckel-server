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

package com.eff3ct.teckel.server

import cats.effect.{Async, Resource}
import cats.implicits._
import com.comcast.ip4s.{Host, Port}
import com.eff3ct.teckel.controller.ApiResponseEnrich
import com.eff3ct.teckel.interface.error.ApiError
import com.eff3ct.teckel.server.config.ApiConfig
import com.eff3ct.teckel.server.route.{HealthRoutes, TeckelRoutes}
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.{HttpApp, Response, Status}
import org.typelevel.log4cats.Logger

object Server {

  def build[F[_]: Async: Network: Logger](apiConf: ApiConfig): Resource[F, Server] = {

    // Definition of Routes layers
    val healthRoutes = HealthRoutes.impl[F]
    val teckelRoutes = TeckelRoutes.impl[F]

    // HTTP App
    val httpApp: HttpApp[F] = (
      healthRoutes <+> teckelRoutes
    ).orNotFound

    // Error handler in case of something wrong
    val errorHandler: PartialFunction[Throwable, F[Response[F]]] = { case th: Throwable =>
      Logger[F].error(th)(th.getMessage) *> ApiError(
        Status.InternalServerError.code,
        s"InternalServerError: $th"
      )
        .asLeft[Unit]
        .toHttp4sResponse(Status.InternalServerError)
    }

    // Server builder
    val serverBuilder: EmberServerBuilder[F] =
      EmberServerBuilder
        .default[F]
        .withHostOption(Host.fromString(apiConf.host))
        .withPort(Port.fromInt(apiConf.port).get)
        .withHttpApp(httpApp)
        .withErrorHandler(errorHandler)

    serverBuilder.build
  }
}
