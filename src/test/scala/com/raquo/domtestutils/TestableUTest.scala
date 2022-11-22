package com.raquo.domtestutils

import com.raquo.domtestutils.matching.ExpectedNode.element
import com.raquo.domtestutils.utestspec.MountUTest
import org.scalajs.dom
import utest._

object TestableUTest extends BaseUnitSuiteLike with MountUTest {

  override def tests = Tests {
    test("Empty Seq uTest") {
      assert(Seq.empty[Int].isEmpty)
    }
    test("expectNode uTest") {
      mount(dom.document.createElement("div"))
      expectNode(
        element("div")
      )
    }
  }
}
