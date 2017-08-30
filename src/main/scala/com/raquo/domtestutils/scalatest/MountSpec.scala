package com.raquo.domtestutils.scalatest

import com.raquo.domtestutils.MountOps
import org.scalatest.{Outcome, TestSuite}

trait MountSpec[N]
  extends TestSuite
  with MountOps[N]
{

  override def doAssert(condition: Boolean, message: String): Unit = {
    assert(condition, message)
  }

  override def doFail(message: String): Nothing = {
    fail(message)
  }

  /** Note: we use withFixture instead of beforeEach/afterEach because
    * ScalaTest obscures error messages reported from the latter.
    */
  override def withFixture(test: NoArgTest): Outcome = {
    resetDOM() // Runs in the beginning of each test
    try {
      super.withFixture(test)
    } finally {
      clearDOM() // Runs at the end of each test, regardless of the result
    }
  }
}
