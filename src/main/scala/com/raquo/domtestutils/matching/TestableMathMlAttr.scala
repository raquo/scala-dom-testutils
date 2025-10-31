package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import org.scalajs.dom

class TestableMathMlAttr[V](
  val name: String,
  val encode: V => String,
  val decode: String => V
) {

  def is(expectedValue: V): Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeAttrIs(Some(expectedValue)))
  }

  def isEmpty: Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeAttrIs(None))
  }

  private[domtestutils] def nodeAttrIs(maybeExpectedValue: Option[V])(node: dom.Node): MaybeError = {
    node match {

      // #TODO JSDOM does not implement MathML so...
      //  - https://github.com/jsdom/jsdom/issues/3515
      //  - Given that, testing it is kinda useless, but at least we can test the Laminar parts / types...
      //
      // case element: dom.MathMLElement =>     // dom.MathMLElement class does not exist in JSDOM
      case element if element.namespaceURI == "http://www.w3.org/1998/Math/MathML" =>
        val maybeActualValue = getAttr(element.asInstanceOf[dom.MathMLElement])
        (maybeActualValue, maybeExpectedValue) match {

          case (Some(actualValue), Some(expectedValue)) =>
            if (actualValue == expectedValue) {
              None
            } else {
              Some(s"""|MathML Attr `${name}` value is incorrect:
                       |- Actual:   ${repr(actualValue)}
                       |- Expected: ${repr(expectedValue)}
                       |""".stripMargin)
            }

          case (None, Some(expectedValue)) =>
            if (encode(expectedValue) == null) {
              None // Note: `encode` returning `null` is exactly how missing attribute values are defined, e.g. in BooleanAsAttrPresenceCodec
            } else {
              Some(s"""|MathML Attr `${name}` is missing:
                       |- Actual:   (no attribute)
                       |- Expected: ${repr(expectedValue)}
                       |""".stripMargin)
            }

          case (Some(actualValue), None) =>
            Some(s"""|MathML Attr `${name}` should not be present:
                     |- Actual:   ${repr(actualValue)}
                     |- Expected: (no attribute)
                     |""".stripMargin)

          case (None, None) =>
            None
        }

      case _ =>
        Some(s"Unable to verify MathML Attr `${name}` because node $node is not a DOM MathML Element (might be a text node or an HTML / SVG element?)")
    }
  }

  private[domtestutils] def getAttr(element: dom.MathMLElement): Option[V] = {
    // Note: for boolean-as-presence attributes, this returns `None` instead of `Some(false)` when the attribute is missing.
    if (element.hasAttribute(name)) {
      Some(decode(element.getAttribute(name)))
    } else {
      None
    }
  }
}
