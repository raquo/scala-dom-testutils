package com.raquo.domtestutils

import com.raquo.domtestutils.matching.ExpectedNode.element
import com.raquo.domtestutils.minitestspec.MountMiniTest
import org.scalajs.dom
import minitest._


object TestableMiniTest extends BaseUnitSuiteLike with MountMiniTest {
  test("Empty Seq MiniTest") { env =>
    assert(Seq.empty[Int].isEmpty, "should be empty")
  }
  test("expectNode uTest") { env =>
    mount(dom.document.createElement("div"))
    expectNode(
      element("div")
    )
  }
}
