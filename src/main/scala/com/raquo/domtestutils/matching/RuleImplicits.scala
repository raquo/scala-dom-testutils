package com.raquo.domtestutils.matching

trait RuleImplicits[Tag, Comment, Prop[_], HtmlAttr[_], SvgAttr[_], Style[_], CompositeHtmlKey, CompositeSvgKey] {

  implicit def makeTagTestable(tag: Tag): ExpectedNode

  implicit def makeCommentBuilderTestable(commentBuilder: () => Comment): ExpectedNode

  implicit def makeAttrTestable[V](attr: HtmlAttr[V]): TestableHtmlAttr[V]

  implicit def makePropTestable[V, _DomV](prop: Prop[V] { type DomV = _DomV }): TestableProp[V, _DomV]

  implicit def makeStyleTestable[V](style: Style[V]): TestableStyleProp[V]

  implicit def makeSvgAttrTestable[V](svgAttr: SvgAttr[V]): TestableSvgAttr[V]

  implicit def makeCompositeHtmlKeyTestable(key: CompositeHtmlKey): TestableCompositeKey

  implicit def makeCompositeSvgKeyTestable(key: CompositeSvgKey): TestableCompositeKey

  // Converters

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

  // Option-based converters

  implicit def maybeExpectedNodeAsExpectedChildRule(maybeExpectedChild: Option[ExpectedNode]): Rule = (expectedParent: ExpectedNode) => {
    maybeExpectedChild.foreach(expectedParent.addExpectedChild)
  }

  implicit def maybeTagAsExpectedChildRule(maybeTag: Option[Tag]): Rule = (expectedParent: ExpectedNode) => {
    maybeTag.foreach(tagAsExpectedChildRule(_).applyTo(expectedParent))
  }

  implicit def maybeCommentBuilderAsExpectedChildRule(maybeCommentBuilder: Option[() => Comment]): Rule = (expectedParent: ExpectedNode) => {
    maybeCommentBuilder.foreach(commentBuilderAsExpectedChildRule(_).applyTo(expectedParent))
  }

  implicit def maybeStringAsExpectedTextRule(maybeChildText: Option[String]): Rule = (expectedParent: ExpectedNode) => {
    maybeChildText.foreach(stringAsExpectedTextRule(_).applyTo(expectedParent))
  }

}
