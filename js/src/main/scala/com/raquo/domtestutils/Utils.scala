package com.raquo.domtestutils

import scala.util.Random

trait Utils {

  def randomString(prefix: String = "", length: Int = 5): String = {
    prefix + Random.nextString(length)
  }

  def repr(value: Any): String = {
    value match {
      case str: String => "\"" + str + "\""
      case _ => value.toString
    }
  }
}

object Utils extends Utils
