package com.raquo.domtestutils.scalatest

import com.raquo.domtestutils.MountOps
import org.scalactic
import org.scalatest.{Outcome, TestSuite}
import org.scalatest.exceptions.StackDepthException
import org.scalatest.exceptions.TestFailedException

trait MountSpec
  extends TestSuite
  with MountOps {

  override def doAssert(
    condition: Boolean,
    message: String
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position
  ): Unit = {
    assert(condition, message)(prettifier, pos, UseDefaultAssertions)
  }

  override def doFail(message: String)(implicit pos: scalactic.source.Position): Nothing = {
    throw new TestFailedException(toExceptionFunction(Some(message)), None, Left(pos), None, Vector.empty)
  }

  /** Note: we use withFixture instead of beforeEach/afterEach because
    * ScalaTest obscures error messages reported from the latter.
    */
  override def withFixture(test: NoArgTest): Outcome = {
    resetDOM("withFixture-begin") // Runs in the beginning of each test
    try {
      super.withFixture(test)
    } finally {
      clearDOM("withFixture-end") // Runs at the end of each test, regardless of the result
    }
  }

  // Copy of ScalaTest's function of the same name, because that one is scalatest-private.
  private def toExceptionFunction(message: Option[String]): StackDepthException => Option[String] = {
    message match {
      case null => throw new org.scalactic.exceptions.NullArgumentException("message was null")
      case Some(null) => throw new org.scalactic.exceptions.NullArgumentException("message was Some(null)")
      case _ => { e => message }
    }
  }
}
