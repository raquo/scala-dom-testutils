package com.raquo.domtestutils

import com.raquo.domtestutils.codecs.{BooleanAsIsCodec, IntAsIsCodec, IterableAsSpaceSeparatedStringCodec, StringAsIsCodec}
import com.raquo.domtestutils.fixtures.Prop
import org.scalajs.dom

import scala.scalajs.js

class TestablePropSpec extends UnitSpec {

  val href = new Prop("href", StringAsIsCodec)
  val tabIndex = new Prop("tabIndex", IntAsIsCodec)
  val disabled = new Prop("disabled", BooleanAsIsCodec)
  val classNames = new Prop("className", IterableAsSpaceSeparatedStringCodec)

  def setProp[V, DomV](el: dom.Element, prop: Prop[V, DomV], value: V): Unit = {
    val domValue = prop.codec.encode(value).asInstanceOf[js.Any]
    el.asInstanceOf[js.Dynamic].updateDynamic(prop.name)(domValue)
  }

  it("href: standard string prop") {
    val el = dom.document.createElement("a").asInstanceOf[dom.html.Anchor]

    (href nodePropIs None) (el) shouldBe None
    (href nodePropIs Some("http://example.com")) (el) shouldBe Some("Prop `href` is empty or missing:\n- Actual (raw): \"\"\n- Expected:     \"http://example.com\"\n")

    setProp(el, href, "http://example.com")
    // Notice that a slash "/" was added to the URL by the "browser" (well, jsdom in this case)
    (href nodePropIs Some("http://example.com/")) (el) shouldBe None
    (href nodePropIs Some("http://expected.com/")) (el) shouldBe Some("Prop `href` value is incorrect:\n- Actual:   \"http://example.com/\"\n- Expected: \"http://expected.com/\"\n")
  }

  it("tabIndex: integer prop") {
    val el = dom.document.createElement("a")

    // 0 is the default value of tabIndex
    (tabIndex nodePropIs Some(0)) (el) shouldBe None
    (tabIndex nodePropIs Some(5)) (el) shouldBe Some("Prop `tabIndex` value is incorrect:\n- Actual:   0\n- Expected: 5\n")

    setProp(el, tabIndex, 10)
    (tabIndex nodePropIs Some(10)) (el) shouldBe None
    (tabIndex nodePropIs Some(5)) (el) shouldBe Some("Prop `tabIndex` value is incorrect:\n- Actual:   10\n- Expected: 5\n")
  }

  it("disabled: boolean prop") {
    val el = dom.document.createElement("a")

    (disabled nodePropIs Some(false)) (el) shouldBe Some("Prop `disabled` is empty or missing:\n- Actual (raw): undefined\n- Expected:     false\n")
    (disabled nodePropIs None) (el) shouldBe None
    (disabled nodePropIs Some(true)) (el) shouldBe Some("Prop `disabled` is empty or missing:\n- Actual (raw): undefined\n- Expected:     true\n")

    setProp(el, disabled, true)
    (disabled nodePropIs Some(true)) (el) shouldBe None
    (disabled nodePropIs Some(false)) (el) shouldBe Some("Prop `disabled` value is incorrect:\n- Actual:   true\n- Expected: false\n")
    (disabled nodePropIs None) (el) shouldBe Some("Prop `disabled` should be empty or not present:\n- Actual:   true\n- Expected: (empty / not present)\n")

    setProp(el, disabled, false)
    (disabled nodePropIs Some(false)) (el) shouldBe None
    (disabled nodePropIs None) (el) shouldBe Some("Prop `disabled` should be empty or not present:\n- Actual:   false\n- Expected: (empty / not present)\n")
    (disabled nodePropIs Some(true)) (el) shouldBe Some("Prop `disabled` value is incorrect:\n- Actual:   false\n- Expected: true\n")
  }

  it("classNames: list as string prop") {
    val el = dom.document.createElement("a")

    (classNames nodePropIs None)(el) shouldBe None
    (classNames nodePropIs Some(Seq("foo", "bar")))(el) shouldBe Some("Prop `className` is empty or missing:\n- Actual (raw): \"\"\n- Expected:     List(foo, bar)\n")

    setProp(el, classNames, Seq("foo", "bar"))
    (classNames nodePropIs Some(List("foo", "bar")))(el) shouldBe None
    // @TODO[Elegance] Scala 2.13 has ArraySeq instead of WrappedArray, so we're making an ugly replace here.
    (classNames nodePropIs Some(List("foo", "bar", "baz")))(el).map(_.replace("WrappedArray", "ArraySeq")) shouldBe Some("Prop `className` value is incorrect:\n- Actual:   ArraySeq(foo, bar)\n- Expected: List(foo, bar, baz)\n")
    (classNames nodePropIs None)(el).map(_.replace("WrappedArray", "ArraySeq")) shouldBe Some("Prop `className` should be empty or not present:\n- Actual:   ArraySeq(foo, bar)\n- Expected: (empty / not present)\n")

    setProp(el, classNames, Seq())
    (classNames nodePropIs None)(el) shouldBe None
    (classNames nodePropIs Some(List("foo", "bar")))(el) shouldBe Some("Prop `className` is empty or missing:\n- Actual (raw): \"\"\n- Expected:     List(foo, bar)\n")
  }
}

