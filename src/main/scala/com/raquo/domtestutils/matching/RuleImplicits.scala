package com.raquo.domtestutils.matching

trait RuleImplicits[ //
  Tag,
  Comment,
  HtmlProp[_],
  GlobalAttr[_],
  HtmlAttr[_],
  SvgAttr[_],
  MathMlAttr[_],
  Style[_],
  CompositeKey,
] {

  implicit def makeTagTestable(tag: Tag): ExpectedNode

  implicit def makeCommentBuilderTestable(commentBuilder: () => Comment): ExpectedNode

  // #TODO[IDE] IntelliJ 2025.2.4 does not pick up this implicit conversion – don't know any workaround – reported minified: https://youtrack.jetbrains.com/issue/SCL-24554/Abstract-type-member-in-function-argument-causes-red-code
  implicit def makeHtmlPropTestable[V, _DomV](prop: HtmlProp[V] { type DomV = _DomV }): TestableHtmlProp[V, _DomV]

  implicit def makeStyleTestable[V](style: Style[V]): TestableStyleProp[V]

  implicit def makeGlobalAttrTestable[V](attr: GlobalAttr[V]): TestableGlobalAttr[V]

  implicit def makeHtmlAttrTestable[V](attr: HtmlAttr[V]): TestableHtmlAttr[V]

  implicit def makeSvgAttrTestable[V](svgAttr: SvgAttr[V]): TestableSvgAttr[V]

  implicit def makeMathMlAttrTestable[V](attr: MathMlAttr[V]): TestableMathMlAttr[V]

  implicit def makeCompositeKeyTestable(key: CompositeKey): TestableCompositeKey

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
