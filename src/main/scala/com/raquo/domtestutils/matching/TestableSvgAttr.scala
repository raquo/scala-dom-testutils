package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import com.raquo.domtypes.generic.keys.SvgAttr
import org.scalajs.dom

class TestableSvgAttr[V](val svgAttr: SvgAttr[V]) extends AnyVal {

  def is(expectedValue: V): Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeSvgAttrIs(Some(expectedValue)))
  }

  def isEmpty: Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeSvgAttrIs(None))
  }

  private[domtestutils] def nodeSvgAttrIs(maybeExpectedValue: Option[V])(node: dom.Node): MaybeError = {
    node match {
      // @TODO[Integrity] we can't make a `node instanceof dom.svg.Element` check b/c jsdom does not implement SVGElement
      case (element: dom.Element) =>
        val maybeActualValue = getSvgAttr(element)
        (maybeActualValue, maybeExpectedValue) match {
          case (None, None) => None
          case (Some(actualValue), Some(expectedValue)) =>
            if (actualValue == expectedValue) {
              None
            } else {
              Some(s"SVG Attr `${svgAttr.name}` value is incorrect: actual value ${repr(actualValue)}, expected value ${repr(expectedValue)}")
            }
          case (None, Some(expectedValue)) =>
            if (svgAttr.codec.encode(expectedValue) == null) {
              None // Note: `encode` returning `null` is exactly how missing attribute values are defined, e.g. in BooleanAsAttrPresenceCodec
            } else {
              Some(s"SVG Attr `${svgAttr.name}` is missing, expected ${repr(expectedValue)}")
            }
          case (Some(actualValue), None) =>
            Some(s"SVG Attr `${svgAttr.name}` should not be present: actual value ${repr(actualValue)}, expected to be missing")
        }
      case _ =>
        Some(s"Unable to verify SVG Attr `${svgAttr.name}` because node $node is not a DOM SVG Element (might be a text node?)")
    }
  }

  private[domtestutils] def getSvgAttr(element: dom.Element): Option[V] = {
    // Note: for boolean-as-presence attributes, this returns `None` instead of `Some(false)` when the attribute is missing.
    if (element.hasAttributeNS(namespaceURI = TestableSvgAttr.svgNamespaceUri, localName = svgAttr.name)) {
      Some(svgAttr.codec.decode(element.getAttributeNS(namespaceURI = TestableSvgAttr.svgNamespaceUri, localName = svgAttr.name)))
    } else {
      None
    }
  }
}

object TestableSvgAttr {
  val svgNamespaceUri = "http://www.w3.org/2000/svg"
}
