package com.raquo.domtestutils.matching

import com.raquo.domtypes.generic.builders.Tag
import com.raquo.domtypes.generic.keys.{Attr, Prop, Style, SvgAttr}
import com.raquo.domtypes.generic.nodes.Comment

trait RuleImplicits {

  implicit def makeTagTestable(tag: Tag[_]): ExpectedNode = {
    ExpectedNode.element(tag)
  }

  implicit def makeCommentBuilderTestable(commentBuilder: () => Comment): ExpectedNode = {
    ExpectedNode.comment()
  }

  implicit def makeAttrTestable[V](attr: Attr[V]): TestableAttr[V] = {
    new TestableAttr(attr)
  }

  implicit def makePropTestable[V, DomV](prop: Prop[V, DomV]): TestableProp[V, DomV] = {
    new TestableProp(prop)
  }

  implicit def makeStyleTestable[V](style: Style[V]): TestableStyle[V] = {
    new TestableStyle(style)
  }

  implicit def makeSvgAttrTestable[V](svgAttr: SvgAttr[V]): TestableSvgAttr[V] = {
    new TestableSvgAttr(svgAttr)
  }

  implicit def expectedNodeAsExpectedChildRule(expectedChild: ExpectedNode): Rule = (expectedParent: ExpectedNode) => {
    expectedParent.addExpectedChild(expectedChild)
  }

  implicit def tagAsExpectedChildRule(tag: Tag[_]): Rule = (expectedParent: ExpectedNode) => {
    val expectedChild: ExpectedNode = makeTagTestable(tag)
    expectedParent.addExpectedChild(expectedChild)
  }

  implicit def commentBuilderAsExpectedChildRule(commentBuilder: () => Comment): Rule = (expectedParent: ExpectedNode) => {
    val expectedChild: ExpectedNode = makeCommentBuilderTestable(commentBuilder)
    expectedParent.addExpectedChild(expectedChild)
  }

  implicit def stringAsExpectedTextRule(childText: String): Rule = (expectedParent: ExpectedNode) => {
    if (expectedParent.isComment) {
      expectedParent.addCheck(ExpectedNode.checkCommentText(childText))
    } else {
      val expectedTextChild = ExpectedNode.textNode()
      expectedTextChild.addCheck(ExpectedNode.checkText(childText))
      expectedParent.addExpectedChild(expectedTextChild)
    }
  }
}
