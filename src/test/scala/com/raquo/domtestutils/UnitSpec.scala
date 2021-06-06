package com.raquo.domtestutils

import com.raquo.domtestutils.matching.{AssertSyntax, RuleImplicits}
import munit.FunSuite

class UnitSpec extends FunSuite with RuleImplicits {

  implicit def useAssertSyntax[A](v: A): AssertSyntax[A] = new AssertSyntax(v)
}

