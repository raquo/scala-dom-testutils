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
    ShouldSyntax.shouldEqual(actual, expected)(equality, pos, prettifier)
  }

  def shouldBe(
    expected: A
  )(
    implicit pos: source.Position,
    prettifier: Prettifier
  ): Assertion = {
    ShouldSyntax.shouldBe(actual, expected)(pos, prettifier)
  }
}

object ShouldSyntax extends should.Matchers {

  // #Note ScalaTest generates different code for Scala 2 and Scala 3.
  //  In particular, it does not emit convertToAnyShouldWrapper in Scala 3.
  //  I don't care enough to figure out what or why.
  //  If you do, see SKIP-DOTTY-START and SKIP-DOTTY-STOP in ScalaTest code and go from there.

  def shouldEqual[A](
    actual: A,
    expected: scala.Any
  )(
    implicit equality : org.scalactic.Equality[A],
    pos : source.Position,
    prettifier : Prettifier
  ): Assertion = {
    actual shouldEqual expected
  }

  def shouldBe[A](
    actual: A,
    expected: scala.Any
  )(
    implicit pos: source.Position,
    prettifier: Prettifier
  ): Assertion = {
    actual shouldBe expected
  }
}
