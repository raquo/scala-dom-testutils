package com.raquo.domtestutils.mu

import com.raquo.domtestutils.MountOps

abstract class MountSpec
  extends munit.FunSuite
  with MountOps {

  override def beforeEach(context: BeforeEach): Unit = {
    // @nc test an exception here
    resetDOM("withFixture-begin")
  }

  override def afterEach(context: AfterEach): Unit = {
    clearDOM("withFixture-end") // Runs at the end of each test, regardless of the result
  }

  override def doAssert(condition: Boolean, message: String): Unit = {
    assert(condition, message)
  }

  override def doFail(message: String): Nothing = {
    fail(message)
  }
}
