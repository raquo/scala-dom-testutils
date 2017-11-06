package com.raquo.domtestutils

import scala.util.Random

trait Utils {

  // @TODO[API] this doesn't really belong here
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
