package com.raquo.domtestutils

import com.raquo.domtestutils.matching.TestableSvgAttr
import com.raquo.domtypes.generic.codecs.StringAsIsCodec
import com.raquo.domtypes.generic.keys.SvgAttr
import org.scalajs.dom

class TestableSvgAttrSpec extends UnitSpec {

  val cls = new SvgAttr("className", StringAsIsCodec)

  def setAttr[V](el: dom.Element, svgAttr: SvgAttr[V], value: V): Unit = {
    val domValue = svgAttr.codec.encode(value)
    if (domValue == null) {
      el.removeAttributeNS(TestableSvgAttr.svgNamespaceUri, svgAttr.name)
    } else {
      el.setAttributeNS(TestableSvgAttr.svgNamespaceUri, svgAttr.name, domValue)
    }
  }

  it("cls: standard string attr") {
    val el = dom.document.createElementNS(TestableSvgAttr.svgNamespaceUri, "svg")

    (cls nodeSvgAttrIs None) (el) shouldBe None
    (cls nodeSvgAttrIs Some("class1")) (el) shouldBe Some("SVG Attr `className` is missing, expected \"class1\"")

    setAttr(el, cls, "class1")
    (cls nodeSvgAttrIs Some("class1")) (el) shouldBe None
    (cls nodeSvgAttrIs Some("class2")) (el) shouldBe Some("SVG Attr `className` value is incorrect: actual value \"class1\", expected value \"class2\"")
  }
}
