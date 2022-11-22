package com.raquo.domtestutils.utestspec

import com.raquo.domtestutils.MountOps
import utest.{TestSuite, assert}

trait MountUTest extends TestSuite with MountOps {
    override def doAssert(condition: Boolean, message: String): Unit = assert(condition)

    override def doFail(message: String) = assert(false).asInstanceOf[Nothing]

    override def utestBeforeEach(path: Seq[String]): Unit = resetDOM()

    override def utestAfterEach(path: Seq[String]): Unit = clearDOM()
}
