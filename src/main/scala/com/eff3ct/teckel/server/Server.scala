package com.eff3ct.teckel.server

import cats.effect.{Async, Resource}
import cats.implicits._
import com.comcast.ip4s.{Host, Port}
import com.eff3ct.teckel.controller.ApiResponseEnrich
import com.eff3ct.teckel.interface.error.ApiError
import com.eff3ct.teckel.server.config.ApiConfig
import com.eff3ct.teckel.server.route.TeckelRoutes
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.{HttpApp, Response, Status}
import org.typelevel.log4cats.Logger

object Server {

  def build[F[_]: Async: Network: Logger](apiConf: ApiConfig): Resource[F, Server] = {

    // Definition of Routes layers
    val teckelRoutes = TeckelRoutes.impl[F]

    // HTTP App
    val httpApp: HttpApp[F] = (
      teckelRoutes
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
