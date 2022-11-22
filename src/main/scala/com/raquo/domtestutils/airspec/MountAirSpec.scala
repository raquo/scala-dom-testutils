package com.raquo.domtestutils.airspec

import wvlet.airspec.AirSpec

trait MountAirSpec extends AirSpec with com.raquo.domtestutils.MountOps {
  override def doAssert(condition: Boolean, message: String): Unit = assert(condition, message)

  override def doFail(message: String) = fail(message).asInstanceOf[Nothing]

  //@com.github.ghik.silencer.silent // workaround when "-Xfatal-warnings" enabled
  override protected def before: Unit = resetDOM()
  //@com.github.ghik.silencer.silent
  override protected def after: Unit = clearDOM()
}
