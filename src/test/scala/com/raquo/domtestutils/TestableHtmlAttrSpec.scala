package com.raquo.domtestutils

import com.raquo.domtypes.generic.codecs.{BooleanAsAttrPresenceCodec, BooleanAsTrueFalseStringCodec, IntAsStringCodec, StringAsIsCodec}
import com.raquo.domtypes.generic.keys.{HtmlAttr}
import org.scalajs.dom

class TestableHtmlAttrSpec extends UnitSpec {

  val href = new HtmlAttr("href", StringAsIsCodec)
  val tabIndex = new HtmlAttr("tabindex", IntAsStringCodec)
  val disabled = new HtmlAttr("disabled", BooleanAsAttrPresenceCodec)
  val contentEditable = new HtmlAttr("contenteditable", BooleanAsTrueFalseStringCodec)

  def setAttr[V](el: dom.Element, attr: HtmlAttr[V], value: V): Unit = {
    val domValue = attr.codec.encode(value)
    if (domValue == null) {
      el.removeAttribute(attr.name)
    } else {
      el.setAttribute(attr.name, domValue)
    }
  }

  test("href: standard string attr") {
    val el = dom.document.createElement("a")

    (href nodeAttrIs None) (el) shouldBe None
    (href nodeAttrIs Some("http://example.com")) (el) shouldBe Some("Attr `href` is missing:\n- Actual:   (no attribute)\n- Expected: \"http://example.com\"\n")

    setAttr(el, href, "http://example.com")
    (href nodeAttrIs Some("http://example.com")) (el) shouldBe None
    (href nodeAttrIs Some("http://expected.com")) (el) shouldBe Some("Attr `href` value is incorrect:\n- Actual:   \"http://example.com\"\n- Expected: \"http://expected.com\"\n")
  }

  test("tabIndex: integer attr") {
    val el = dom.document.createElement("a")

    (tabIndex nodeAttrIs None) (el) shouldBe None
    (tabIndex nodeAttrIs Some(5)) (el) shouldBe Some("Attr `tabindex` is missing:\n- Actual:   (no attribute)\n- Expected: 5\n")

    setAttr(el, tabIndex, 10)
    (tabIndex nodeAttrIs Some(10)) (el) shouldBe None
    (tabIndex nodeAttrIs Some(5)) (el) shouldBe Some("Attr `tabindex` value is incorrect:\n- Actual:   10\n- Expected: 5\n")
  }

  test("disabled: boolean-as-presence (absence is the same as false)") {
    val el = dom.document.createElement("a")

    (disabled nodeAttrIs Some(false)) (el) shouldBe None
    (disabled nodeAttrIs None) (el) shouldBe None
    (disabled nodeAttrIs Some(true)) (el) shouldBe Some("Attr `disabled` is missing:\n- Actual:   (no attribute)\n- Expected: true\n")

    setAttr(el, disabled, true)
    (disabled nodeAttrIs Some(true)) (el) shouldBe None
    (disabled nodeAttrIs Some(false)) (el) shouldBe Some("Attr `disabled` value is incorrect:\n- Actual:   true\n- Expected: false\n")
    (disabled nodeAttrIs None) (el) shouldBe Some("Attr `disabled` should not be present:\n- Actual:   true\n- Expected: (no attribute)\n")

    setAttr(el, disabled, false)
    (disabled nodeAttrIs Some(false)) (el) shouldBe None
    (disabled nodeAttrIs None) (el) shouldBe None
    (disabled nodeAttrIs Some(true)) (el) shouldBe Some("Attr `disabled` is missing:\n- Actual:   (no attribute)\n- Expected: true\n")
  }

  test("contentEditable: boolean as true/false string attr") {
    val el = dom.document.createElement("a")

    (contentEditable nodeAttrIs Some(false))(el) shouldBe Some("Attr `contenteditable` is missing:\n- Actual:   (no attribute)\n- Expected: false\n")
    (contentEditable nodeAttrIs None)(el) shouldBe None
    (contentEditable nodeAttrIs Some(true))(el) shouldBe Some("Attr `contenteditable` is missing:\n- Actual:   (no attribute)\n- Expected: true\n")

    setAttr(el, contentEditable, true)
    (contentEditable nodeAttrIs Some(true))(el) shouldBe None
    (contentEditable nodeAttrIs Some(false))(el) shouldBe Some("Attr `contenteditable` value is incorrect:\n- Actual:   true\n- Expected: false\n")
    (contentEditable nodeAttrIs None)(el) shouldBe Some("Attr `contenteditable` should not be present:\n- Actual:   true\n- Expected: (no attribute)\n")

    setAttr(el, contentEditable, false)
    (contentEditable nodeAttrIs Some(false))(el) shouldBe None
    (contentEditable nodeAttrIs None)(el) shouldBe Some("Attr `contenteditable` should not be present:\n- Actual:   false\n- Expected: (no attribute)\n")
    (contentEditable nodeAttrIs Some(true))(el) shouldBe Some("Attr `contenteditable` value is incorrect:\n- Actual:   false\n- Expected: true\n")

    el.removeAttribute(contentEditable.name)
    (contentEditable nodeAttrIs Some(false))(el) shouldBe Some("Attr `contenteditable` is missing:\n- Actual:   (no attribute)\n- Expected: false\n")
    (contentEditable nodeAttrIs None)(el) shouldBe None
    (contentEditable nodeAttrIs Some(true))(el) shouldBe Some("Attr `contenteditable` is missing:\n- Actual:   (no attribute)\n- Expected: true\n")
  }
}
