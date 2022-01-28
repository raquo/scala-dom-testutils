package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import com.raquo.domtypes.generic.codecs.Codec
import org.scalajs.dom

class TestableHtmlAttr[V](
  val name: String,
  val codec: Codec[V, String]
) {

  def is(expectedValue: V): Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeAttrIs(Some(expectedValue)))
  }

  def isEmpty: Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeAttrIs(None))
  }

  private[domtestutils] def nodeAttrIs(maybeExpectedValue: Option[V])(node: dom.Node): MaybeError = {
    node match {

      case (element: dom.html.Element) =>
        val maybeActualValue = getAttr(element)
        (maybeActualValue, maybeExpectedValue) match {

          case (Some(actualValue), Some(expectedValue)) =>
            if (actualValue == expectedValue) {
              None
            } else {
              Some(s"""|Attr `${name}` value is incorrect:
                       |- Actual:   ${repr(actualValue)}
                       |- Expected: ${repr(expectedValue)}
                       |""".stripMargin)
            }

          case (None, Some(expectedValue)) =>
            if (codec.encode(expectedValue) == null) {
              None // Note: `encode` returning `null` is exactly how missing attribute values are defined, e.g. in BooleanAsAttrPresenceCodec
            } else {
              Some(s"""|Attr `${name}` is missing:
                       |- Actual:   (no attribute)
                       |- Expected: ${repr(expectedValue)}
                       |""".stripMargin)
            }

          case (Some(actualValue), None) =>
            Some(s"""|Attr `${name}` should not be present:
                     |- Actual:   ${repr(actualValue)}
                     |- Expected: (no attribute)
                     |""".stripMargin)

          case (None, None) =>
            None
        }

      case _ =>
        Some(s"Unable to verify Attr `${name}` because node $node is not a DOM HTML Element (might be a text node?)")
    }
  }

  private[domtestutils] def getAttr(element: dom.html.Element): Option[V] = {
    // Note: for boolean-as-presence attributes, this returns `None` instead of `Some(false)` when the attribute is missing.
    if (element.hasAttribute(name)) {
      Some(codec.decode(element.getAttribute(name)))
    } else {
      None
    }
  }
}
