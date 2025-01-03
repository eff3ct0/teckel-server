package com.eff3ct.teckel

import cats.effect.{Async, Sync}
import cats.implicits._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j._
import pureconfig.ConfigReader.Result
import pureconfig.error.ConfigReaderFailures

package object server {

  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jFactory.create[F].getLogger

  private def handlerError[F[_]: Async, T](result: Result[T]): F[T] =
    result match {
      case Right(dbc) => Async[F].pure(dbc)
      case Left(err: ConfigReaderFailures) =>
        new NoSuchFieldError(err.prettyPrint(0)).raiseError[F, T]
    }

  implicit class ResultImplicits[T](result: Result[T]) {
    def handler[F[_]: Async]: F[T] = handlerError(result)
  }
}
