package com.eff3ct.teckel.server

import cats.effect.{ExitCode, IO, IOApp}
import com.eff3ct.teckel.server.config.ApiConfig
import org.typelevel.log4cats.Logger

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _         <- Logger[IO].info("Getting api configuration")
      apiConfig <- ApiConfig.load().handler[IO]

      _      <- Logger[IO].info("Setting up the api server")
      result <- Server.build[IO](apiConfig).use(_ => IO.never.as(ExitCode.Success))
    } yield result

}
