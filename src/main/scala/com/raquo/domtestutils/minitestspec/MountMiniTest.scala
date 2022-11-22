package com.raquo.domtestutils.minitestspec

import com.raquo.domtestutils.MountOps
import minitest._
import minitest.api.Void

trait MountMiniTest extends TestSuite[Void] with MountOps {
  override def doAssert(condition: Boolean, message: String): Unit = assert(condition, message)

  override def doFail(message: String) = fail(message).asInstanceOf[Nothing]

  def setup(): Void  = {resetDOM()}

  def tearDown(env: Void): Unit = clearDOM()
}
