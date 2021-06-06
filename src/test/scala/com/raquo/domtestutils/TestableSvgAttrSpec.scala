package com.raquo.domtestutils

import com.raquo.domtypes.generic.codecs.StringAsIsCodec
import com.raquo.domtypes.generic.keys.SvgAttr
import org.scalajs.dom

class TestableSvgAttrSpec extends UnitSpec {

  val svgNamespaceUri = "http://www.w3.org/2000/svg"

  val cls = new SvgAttr("className", StringAsIsCodec, namespace = None)

  val xlinkHref = new SvgAttr("xlink:href", StringAsIsCodec, namespace = Some("http://www.w3.org/1999/xlink"))

  def setAttr[V](el: dom.Element, svgAttr: SvgAttr[V], value: V): Unit = {
    val domValue = svgAttr.codec.encode(value)
    if (domValue == null) {
      el.removeAttributeNS(svgAttr.namespace.orNull, localName = svgAttr.localName)
    } else {
      el.setAttributeNS(svgAttr.namespace.orNull, qualifiedName = svgAttr.name, domValue)
    }
  }

  test("cls: standard string attr") {
    val el = dom.document.createElementNS(svgNamespaceUri, "svg")

    (cls nodeSvgAttrIs None) (el) shouldBe None
    (cls nodeSvgAttrIs Some("class1")) (el) shouldBe Some("SVG Attr `className` is missing:\n- Actual:   (no attribute)\n- Expected: \"class1\"\n")

    setAttr(el, cls, "class1")
    (cls nodeSvgAttrIs Some("class1")) (el) shouldBe None
    (cls nodeSvgAttrIs Some("class2")) (el) shouldBe Some("SVG Attr `className` value is incorrect:\n- Actual:   \"class1\"\n- Expected: \"class2\"\n")
  }

  test("xlinkHref: namespaced string attr") {
    val el = dom.document.createElementNS(svgNamespaceUri, "svg")

    (xlinkHref nodeSvgAttrIs None) (el) shouldBe None
    (xlinkHref nodeSvgAttrIs Some("http://example.com/1")) (el) shouldBe Some("SVG Attr `xlink:href` is missing:\n- Actual:   (no attribute)\n- Expected: \"http://example.com/1\"\n")

    setAttr(el, xlinkHref, "http://example.com/1")
    (xlinkHref nodeSvgAttrIs Some("http://example.com/1")) (el) shouldBe None
    (xlinkHref nodeSvgAttrIs Some("http://example.com/2")) (el) shouldBe Some("SVG Attr `xlink:href` value is incorrect:\n- Actual:   \"http://example.com/1\"\n- Expected: \"http://example.com/2\"\n")
  }
}
