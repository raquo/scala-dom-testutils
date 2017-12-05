package com.raquo.domtestutils.scalatest

import com.raquo.domtestutils.MountOps
import org.scalatest.{AsyncTestSuite, FutureOutcome}

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext

trait AsyncMountSpec
  extends AsyncTestSuite
  with MountOps {

  implicit override def executionContext: ExecutionContext = JSExecutionContext.queue

  override def doAssert(condition: Boolean, message: String): Unit = {
    assert(condition, message)
  }

  override def doFail(message: String): Nothing = {
    fail(message)
  }

  /** Note: we use withFixture instead of beforeEach/afterEach because
    * ScalaTest obscures error messages reported from the latter.
    */
  override def withFixture(test: NoArgAsyncTest): FutureOutcome = {
    resetDOM() // Runs in the beginning of each test
    super.withFixture(test).onCompletedThen(_ => {
      clearDOM() // Runs at the end of each test, regardless of the result
    })
  }
}
