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
