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

  it("href: standard string attr") {
    val el = dom.document.createElement("a")

    (href nodeAttrIs None) (el) shouldBe None
    (href nodeAttrIs Some("http://example.com")) (el) shouldBe Some("Attr `href` is missing, expected \"http://example.com\"")

    setAttr(el, href, "http://example.com")
    (href nodeAttrIs Some("http://example.com")) (el) shouldBe None
    (href nodeAttrIs Some("http://expected.com")) (el) shouldBe Some("Attr `href` value is incorrect: actual value \"http://example.com\", expected value \"http://expected.com\"")
  }

  it ("tabIndex: integer attr") {
    val el = dom.document.createElement("a")

    (tabIndex nodeAttrIs None) (el) shouldBe None
    (tabIndex nodeAttrIs Some(5)) (el) shouldBe Some("Attr `tabindex` is missing, expected 5")

    setAttr(el, tabIndex, 10)
    (tabIndex nodeAttrIs Some(10)) (el) shouldBe None
    (tabIndex nodeAttrIs Some(5)) (el) shouldBe Some("Attr `tabindex` value is incorrect: actual value 10, expected value 5")
  }

  it ("disabled: boolean-as-presence (absence is the same as false)") {
    val el = dom.document.createElement("a")

    (disabled nodeAttrIs Some(false)) (el) shouldBe None
    (disabled nodeAttrIs None) (el) shouldBe None
    (disabled nodeAttrIs Some(true)) (el) shouldBe Some("Attr `disabled` is missing, expected true")

    setAttr(el, disabled, true)
    (disabled nodeAttrIs Some(true)) (el) shouldBe None
    (disabled nodeAttrIs Some(false)) (el) shouldBe Some("Attr `disabled` value is incorrect: actual value true, expected value false")
    (disabled nodeAttrIs None) (el) shouldBe Some("Attr `disabled` should not be present: actual value true, expected to be missing")

    setAttr(el, disabled, false)
    (disabled nodeAttrIs Some(false)) (el) shouldBe None
    (disabled nodeAttrIs None) (el) shouldBe None
    (disabled nodeAttrIs Some(true)) (el) shouldBe Some("Attr `disabled` is missing, expected true")
  }

  it ("contentEditable: boolean as true/false string attr") {
    val el = dom.document.createElement("a")

    (contentEditable nodeAttrIs Some(false))(el) shouldBe Some("Attr `contenteditable` is missing, expected false")
    (contentEditable nodeAttrIs None)(el) shouldBe None
    (contentEditable nodeAttrIs Some(true))(el) shouldBe Some("Attr `contenteditable` is missing, expected true")

    setAttr(el, contentEditable, true)
    (contentEditable nodeAttrIs Some(true))(el) shouldBe None
    (contentEditable nodeAttrIs Some(false))(el) shouldBe Some("Attr `contenteditable` value is incorrect: actual value true, expected value false")
    (contentEditable nodeAttrIs None)(el) shouldBe Some("Attr `contenteditable` should not be present: actual value true, expected to be missing")

    setAttr(el, contentEditable, false)
    (contentEditable nodeAttrIs Some(false))(el) shouldBe None
    (contentEditable nodeAttrIs None)(el) shouldBe Some("Attr `contenteditable` should not be present: actual value false, expected to be missing")
    (contentEditable nodeAttrIs Some(true))(el) shouldBe Some("Attr `contenteditable` value is incorrect: actual value false, expected value true")

    el.removeAttribute(contentEditable.name)
    (contentEditable nodeAttrIs Some(false))(el) shouldBe Some("Attr `contenteditable` is missing, expected false")
    (contentEditable nodeAttrIs None)(el) shouldBe None
    (contentEditable nodeAttrIs Some(true))(el) shouldBe Some("Attr `contenteditable` is missing, expected true")
  }
}
