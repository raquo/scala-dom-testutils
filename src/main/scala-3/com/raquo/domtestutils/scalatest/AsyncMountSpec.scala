package com.raquo.domtestutils.scalatest

import com.raquo.domtestutils.MountOps
import org.scalactic
import org.scalatest.{AsyncTestSuite, FutureOutcome}
import org.scalatest.exceptions.StackDepthException
import org.scalatest.exceptions.TestFailedException

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext

trait AsyncMountSpec
  extends AsyncTestSuite
    with MountOps {

  implicit override def executionContext: ExecutionContext = JSExecutionContext.queue

  override def doAssert(condition: Boolean, message: String)(implicit prettifier: scalactic.Prettifier, pos: scalactic.source.Position): Unit = {
    assert(condition, message)(prettifier, pos, UseDefaultAssertions)
  }

  override def doFail(message: String)(implicit pos: scalactic.source.Position): Nothing = {
    throw new TestFailedException(toExceptionFunction(Some(message)), None, Left(pos), None, Vector.empty)
  }

  /** Note: we use withFixture instead of beforeEach/afterEach because
    * ScalaTest obscures error messages reported from the latter.
    */
  override def withFixture(test: NoArgAsyncTest): FutureOutcome = {
    resetDOM("async-withFixture-begin") // Runs in the beginning of each test
    super.withFixture(test).onCompletedThen(_ => {
      clearDOM("async-withFixture-end") // Runs at the end of each test, regardless of the result
    })
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
