package com.raquo.domtestutils.matching

trait RuleImplicits[Tag, Comment, Prop[_, _], HtmlAttr[_], SvgAttr[_], Style[_]] {

  implicit def makeTagTestable(tag: Tag): ExpectedNode

  implicit def makeCommentBuilderTestable(commentBuilder: () => Comment): ExpectedNode

  implicit def makeAttrTestable[V](attr: HtmlAttr[V]): TestableHtmlAttr[V]

  implicit def makePropTestable[V, DomV](prop: Prop[V, DomV]): TestableProp[V, DomV]

  implicit def makeStyleTestable[V](style: Style[V]): TestableStyleProp[V]

  implicit def makeSvgAttrTestable[V](svgAttr: SvgAttr[V]): TestableSvgAttr[V]

  implicit def expectedNodeAsExpectedChildRule(expectedChild: ExpectedNode): Rule = (expectedParent: ExpectedNode) => {
    expectedParent.addExpectedChild(expectedChild)
  }

  implicit def tagAsExpectedChildRule(tag: Tag): Rule = (expectedParent: ExpectedNode) => {
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
      val expectedTextChild = ExpectedNode.textNode
      expectedTextChild.addCheck(ExpectedNode.checkText(childText))
      expectedParent.addExpectedChild(expectedTextChild)
    }
  }
}
