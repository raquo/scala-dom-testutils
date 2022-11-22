package com.raquo.domtestutils

import com.raquo.domtestutils.airspec.MountAirSpec
import org.scalajs.dom
import com.raquo.domtestutils.matching._
import com.raquo.domtestutils.matching.ExpectedNode._
import com.raquo.domtestutils.fixtures.{Comment, HtmlAttr, Prop, StyleProp, SvgAttr, Tag}
import com.raquo.domtestutils.matching.{ExpectedNode, RuleImplicits, TestableHtmlAttr, TestableProp, TestableStyleProp, TestableSvgAttr}

import wvlet.airspec.AirSpec

trait AirUnitSpec extends AirSpec with BaseUnitSuiteLike
class TestableAirSpec extends AirUnitSpec with MountAirSpec {
  test("Empty Seq AirSpec") {
    assert(Seq.empty[Int].isEmpty)
  }
  test("expectNode AirSpec") {
    mount(dom.document.createElement("div"))
    expectNode(
      element("div")
    )
  }
}
