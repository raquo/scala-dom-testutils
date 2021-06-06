package com.raquo.domtestutils.matching

import munit.Assertions.assertEquals
import munit.Location

class AssertSyntax[A](val v: A) extends AnyVal {

  def shouldBe[B](v2: B)(implicit loc: Location, ev: B <:< A): Unit = assertEquals(v, v2)(loc, ev)
}
