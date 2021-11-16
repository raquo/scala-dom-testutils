package com.raquo.domtestutils.scalatest

import org.scalactic.{Prettifier, source}
import org.scalatest.Assertion
import org.scalatest.matchers.should

class ShouldSyntax[A](val actual: A) extends AnyVal {

  def shouldEqual(
    expected : scala.Any
  )(
    implicit equality : org.scalactic.Equality[A],
    pos : source.Position,
    prettifier : Prettifier
  ): Assertion = {
    new should.Matchers.AnyShouldWrapper(actual, pos, prettifier).shouldEqual(expected)
  }

  def shouldBe(
    expected: A
  )(
    implicit pos: source.Position,
    prettifier: Prettifier
  ): Assertion = {
    new should.Matchers.AnyShouldWrapper(actual, pos, prettifier).shouldBe(expected)
  }
}
