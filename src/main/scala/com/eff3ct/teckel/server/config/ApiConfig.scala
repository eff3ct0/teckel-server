package com.eff3ct.teckel.server.config

import pureconfig.ConfigReader.Result
import pureconfig._
import pureconfig.generic.auto._

case class ApiConfig(host: String, port: Int)
object ApiConfig {
  def load(): Result[ApiConfig] = load("api-config")

  def load(namespace: String): Result[ApiConfig] =
    ConfigSource.default.at(namespace).load[ApiConfig]
}
