package com.eff3ct.teckel.server.route

import cats.effect.kernel.Async
import com.eff3ct.teckel.controller.TeckelController
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.typelevel.log4cats.Logger

object TeckelRoutes {

  def impl[F[_]: Async: Logger]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case data @ POST -> Root / "etl" =>
      TeckelController.execute[F](data)
    }
  }
}
