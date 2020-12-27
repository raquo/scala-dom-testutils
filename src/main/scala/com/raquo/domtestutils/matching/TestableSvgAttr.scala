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

      case (element: dom.svg.Element) =>
        val maybeActualValue = getSvgAttr(element)
        (maybeActualValue, maybeExpectedValue) match {

          case (Some(actualValue), Some(expectedValue)) =>
            if (actualValue == expectedValue) {
              None
            } else {
              Some(s"""|SVG Attr `${svgAttr.name}` value is incorrect:
                       |- Actual:   ${repr(actualValue)}
                       |- Expected: ${repr(expectedValue)}
                       |""".stripMargin)
            }

          case (None, Some(expectedValue)) =>
            if (svgAttr.codec.encode(expectedValue) == null) {
              None // Note: `encode` returning `null` is exactly how missing attribute values are defined, e.g. in BooleanAsAttrPresenceCodec
            } else {
              Some(s"""|SVG Attr `${svgAttr.name}` is missing:
                       |- Actual:   (no attribute)
                       |- Expected: ${repr(expectedValue)}
                       |""".stripMargin)
            }

          case (Some(actualValue), None) =>
            Some(s"""|SVG Attr `${svgAttr.name}` should not be present:
                     |- Actual:   ${repr(actualValue)}
                     |- Expected: (no attribute)
                     |""".stripMargin)

          case (None, None) =>
            None
        }

      case _ =>
        Some(s"Unable to verify SVG Attr `${svgAttr.name}` because node $node is not a DOM SVG Element (might be a text node?)")
    }
  }

  private[domtestutils] def getSvgAttr(element: dom.Element): Option[V] = {
    // Note: for boolean-as-presence attributes, this returns `None` instead of `Some(false)` when the attribute is missing.
    if (element.hasAttributeNS(namespaceURI = svgAttr.namespace.orNull, localName = localName)) {
      Some(svgAttr.codec.decode(element.getAttributeNS(namespaceURI = svgAttr.namespace.orNull, localName = localName)))
    } else {
      None
    }
  }

  private[domtestutils] def localName: String = {
    val nsPrefixLength = svgAttr.name.indexOf(':')
    if (nsPrefixLength > -1) {
      svgAttr.name.substring(nsPrefixLength + 1)
    } else svgAttr.name
  }
}
