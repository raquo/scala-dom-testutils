package com.raquo.domtestutils

import com.raquo.domtestutils.fixtures.{Comment, HtmlAttr, Prop, StyleProp, SvgAttr, Tag}
import com.raquo.domtestutils.matching.{ExpectedNode, RuleImplicits, TestableHtmlAttr, TestableProp, TestableStyleProp, TestableSvgAttr}
import com.raquo.domtestutils.scalatest.Matchers
import org.scalatest.Suite
import org.scalatest.funspec.AnyFunSpecLike

trait UnitSpec extends UnitSuiteLike with AnyFunSpecLike
trait UnitSuiteLike extends Suite with Matchers with BaseUnitSuiteLike

trait BaseUnitSuiteLike extends RuleImplicits[Tag[Any], Comment, Prop, HtmlAttr, SvgAttr, StyleProp] {

  override implicit def makeTagTestable(tag: Tag[Any]): ExpectedNode = {
    ExpectedNode.element(tag.name)
  }

  override implicit def makeCommentBuilderTestable(commentBuilder: () => Comment): ExpectedNode = {
    ExpectedNode.comment
  }

  override implicit def makeAttrTestable[V](attr: HtmlAttr[V]): TestableHtmlAttr[V] = {
    new TestableHtmlAttr[V](attr.name, attr.codec)
  }

  override implicit def makePropTestable[V, DomV](prop: Prop[V, DomV]): TestableProp[V, DomV] = {
    new TestableProp[V, DomV](prop.name, prop.codec)
  }

  override implicit def makeStyleTestable[V](style: StyleProp[V]): TestableStyleProp[V] = {
    new TestableStyleProp[V](style.name)
  }

  override implicit def makeSvgAttrTestable[V](svgAttr: SvgAttr[V]): TestableSvgAttr[V] = {
    new TestableSvgAttr[V](svgAttr.name, svgAttr.codec, svgAttr.namespace)
  }
}

