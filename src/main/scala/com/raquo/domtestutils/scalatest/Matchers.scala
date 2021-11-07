package com.raquo.domtestutils.scalatest

import org.scalatest.Assertion
import org.scalatest.Assertions.assertResult

trait Matchers {

  def assertEquals(
    actual: scala.Any,
    expected: scala.Any
  )(
    implicit prettifier: org.scalactic.Prettifier,
    pos: org.scalactic.source.Position
  ): Assertion = {
    assertResult(expected = expected)(actual = actual)
  }

  def assertEquals(
    actual: scala.Any,
    expected: scala.Any,
    clue: scala.Any
  )(
    implicit prettifier: org.scalactic.Prettifier,
    pos: org.scalactic.source.Position
  ): Assertion = {
    assertResult(expected = expected, clue = clue)(actual = actual)
  }

  // #TODO[Perf] Make this into AnyVal later, I guess
  implicit class ShouldSyntax[A](val actual: A) {

    def shouldBe(
      expected: A
    )(
      implicit prettifier: org.scalactic.Prettifier,
      pos: org.scalactic.source.Position
    ): Assertion = {
      assertResult(expected = expected)(actual = actual)
    }
  }

}
