package com.eff3ct.teckel.interface

object error {

  case class ApiError(code: Int, message: String)

}
