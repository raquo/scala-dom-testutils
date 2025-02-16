package com.raquo.domtestutils

import com.raquo.domtestutils.fixtures.CompositeHtmlKey
import org.scalajs.dom

class TestableCompositeKeySpec extends UnitSpec {

  val cls = new CompositeHtmlKey("class", separator = " ")

  def appendKey[V](el: dom.Element, key: CompositeHtmlKey, value: String): Unit = {
    val currentDomValue = key.getDomValue(el)
    currentDomValue match {
      case Some(domValue) => el.setAttribute(key.name, domValue + " " + value)
      case None => el.setAttribute(key.name, value)
    }
  }

  def removeAttr(el: dom.Element, attr: CompositeHtmlKey): Unit = {
    el.removeAttribute(attr.name)
  }

  it("cls") {
    val el = dom.document.createElement("span")

    (cls nodeKeyIs None) (el) shouldBe None
    (cls nodeKeyIs Some("foo")) (el) shouldBe Some("CompositeKey `class` is empty or missing:\n- Actual (raw): null\n- Expected:     \"foo\"\n")

    appendKey(el, cls, "foo")
    (cls nodeKeyIs Some("foo")) (el) shouldBe None
    (cls nodeKeyIs Some("bar")) (el) shouldBe Some("CompositeKey `class` value is incorrect:\n- Actual:   \"foo\"\n- Expected: \"bar\"\n")
  }

}
