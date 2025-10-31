package com.raquo.domtestutils

import com.raquo.domtestutils.fixtures.{Comment, CompositeHtmlKey, CompositeSvgKey, HtmlAttr, MathMlAttr, Prop, StyleProp, SvgAttr, Tag}
import com.raquo.domtestutils.matching.{ExpectedNode, RuleImplicits, TestableCompositeKey, TestableHtmlAttr, TestableMathMlAttr, TestableProp, TestableStyleProp, TestableSvgAttr}
import com.raquo.domtestutils.scalatest.Matchers
import org.scalajs.dom
import org.scalatest.funspec.AnyFunSpec

class UnitSpec
  extends AnyFunSpec
    with Matchers
    with RuleImplicits[ //
      Tag[Any],
      Comment,
      Prop,
      HtmlAttr,
      SvgAttr,
      MathMlAttr,
      StyleProp,
      CompositeHtmlKey,
      CompositeSvgKey
    ] {

  override implicit def makeTagTestable(tag: Tag[Any]): ExpectedNode = {
    ExpectedNode.element(tag.name)
  }

  override implicit def makeCommentBuilderTestable(commentBuilder: () => Comment): ExpectedNode = {
    ExpectedNode.comment
  }

  override implicit def makeHtmlAttrTestable[V](attr: HtmlAttr[V]): TestableHtmlAttr[V] = {
    new TestableHtmlAttr[V](attr.name, attr.codec.encode, attr.codec.decode)
  }

  override implicit def makePropTestable[V, _DomV](prop: Prop[V] { type DomV = _DomV }): TestableProp[V, _DomV] = {
    new TestableProp[V, _DomV](prop.name, v => prop.codec.decode(v))
  }

  override implicit def makeStyleTestable[V](style: StyleProp[V]): TestableStyleProp[V] = {
    new TestableStyleProp[V](style.name)
  }

  override implicit def makeSvgAttrTestable[V](svgAttr: SvgAttr[V]): TestableSvgAttr[V] = {
    new TestableSvgAttr[V](svgAttr.name, svgAttr.codec.encode, svgAttr.codec.decode, svgAttr.namespace)
  }

  override implicit def makeMathMlAttrTestable[V](attr: MathMlAttr[V]): TestableMathMlAttr[V] = {
    new TestableMathMlAttr[V](attr.name, attr.codec.encode, attr.codec.decode)
  }

  override implicit def makeCompositeHtmlKeyTestable(key: CompositeHtmlKey): TestableCompositeKey = {
    new TestableCompositeKey(key.name, key.separator, getRawDomValue = {
      case htmlEl: dom.html.Element => htmlEl.getAttribute(key.name)
    })
  }

  override implicit def makeCompositeSvgKeyTestable(key: CompositeSvgKey): TestableCompositeKey = {
    new TestableCompositeKey(key.name, key.separator, getRawDomValue = {
      case svgEl: dom.svg.Element => svgEl.getAttributeNS(namespaceURI = null, key.name)
    })
  }
}

