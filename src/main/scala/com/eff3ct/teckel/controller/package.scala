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

package com.eff3ct.teckel

import cats.data.EitherT
import cats.effect.Concurrent
import cats.implicits._
import cats.{Applicative, Monad}
import com.eff3ct.teckel.interface.error.ApiError
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}
import org.http4s.Status.UnprocessableEntity
import org.http4s.{EntityDecoder, EntityEncoder, Request, Response, Status}

package object controller {

  implicit def decodeEither[A: Decoder, B: Decoder]: Decoder[Either[A, B]] =
    implicitly[Decoder[A]].either(implicitly[Decoder[B]])

  implicit def encodeEither[A, B](implicit l: Encoder[A], r: Encoder[B]): Encoder[Either[A, B]] =
    Encoder.instance { either: Either[A, B] =>
      either.fold(_.asJson, _.asJson)
    }

  implicit class ApiResponseEnrichT[T, F[_]: Monad](apiResponse: EitherT[F, ApiError, T]) {
    def toHttp4sResponse(implicit encoder: EntityEncoder[F, T]): F[Response[F]] =
      apiResponse.value.flatMap(_.toHttp4sResponse[F])

    def toHttp4sResponse(
        statusWhenRight: Status
    )(implicit encoder: EntityEncoder[F, T]): F[Response[F]] =
      apiResponse.value.flatMap(_.toHttp4sResponse[F](statusWhenRight))
  }

  implicit class ApiResponseEnrich[T](apiResponse: Either[ApiError, T]) {
    def toHttp4sResponse[F[_]: Applicative](implicit encoder: EntityEncoder[F, T]): F[Response[F]] =
      apiResponse.toHttp4sResponse(Status.Ok)

    def toHttp4sResponse[F[_]: Applicative](
        statusWhenRight: Status
    )(implicit encoder: EntityEncoder[F, T]): F[Response[F]] = {
      implicitly[Applicative[F]].pure(
        apiResponse.fold(
          {
            case ApiError(Status.BadRequest.code, message) =>
              Response[F](status = Status.BadRequest).withEntity(message)
            case ApiError(Status.NotFound.code, message) =>
              Response[F](status = Status.NotFound).withEntity(message)
            case ApiError(Status.UnprocessableEntity.code, message) =>
              Response[F](status = Status.UnprocessableEntity).withEntity(message)
            case ApiError(Status.Unauthorized.code, message) =>
              Response[F](status = Status.Unauthorized).withEntity(message)
            case apiError =>
              Response[F](status = Status.InternalServerError).withEntity(apiError.message)
          },
          {
            case ()   => Response[F](status = Status.NoContent)
            case body => Response[F](status = statusWhenRight).withEntity(body)
          }
        )
      )
    }
  }

  implicit class Http4sRequestEnrich[F[_]: Concurrent](request: Request[F]) {

    def asEither[T](implicit decoder: EntityDecoder[F, T]): F[Either[ApiError, T]] =
      request
        .attemptAs[T]
        .toValidated
        .map(_.toEither)
        .map(_.leftMap(decodeFailure => ApiError(UnprocessableEntity.code, decodeFailure.message)))
  }
}
