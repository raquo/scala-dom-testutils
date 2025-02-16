package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import org.scalajs.dom
import org.scalajs.dom.Element

class TestableCompositeKey(
  val name: String,
  val separator: String,
  getRawDomValue: PartialFunction[dom.Element, String]
) {

  def is(expectedValue: String): Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeKeyIs(maybeExpectedValue = Some(expectedValue)))
  }

  def isEmpty: Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeKeyIs(maybeExpectedValue = None))
  }

  private[domtestutils] def nodeKeyIs(maybeExpectedValue: Option[String])(node: dom.Node): MaybeError = {
    val maybeActualValue = getDomValue(node)
    node match {
      case element: Element =>
        (maybeActualValue, maybeExpectedValue) match {

          case (Some(actualValue), Some(expectedValue)) =>
            if (actualValue == expectedValue) {
              None
            } else {
              Some(
                s"""|CompositeKey `${name}` value is incorrect:
                    |- Actual:   ${repr(actualValue)}
                    |- Expected: ${repr(expectedValue)}
                    |""".stripMargin)
            }

          case (None, Some(expectedValue)) =>
            val rawActualValue = getRawDomValue.applyOrElse(element, default = null)
            Some(
              s"""|CompositeKey `${name}` is empty or missing:
                  |- Actual (raw): ${repr(rawActualValue)}
                  |- Expected:     ${repr(expectedValue)}
                  |""".stripMargin)

          case (Some(actualValue), None) =>
            Some(
              s"""|CompositeKey `${name}` should be empty or not present:
                  |- Actual:   ${repr(actualValue)}
                  |- Expected: (empty / not present)
                  |""".stripMargin)

          case (None, None) =>
            None
        }
      case _ =>
        Some(s"Unable to verify CompositeKey `${name}` because node $node is not a DOM Element (might be a text node?)")
    }
  }

  private[domtestutils] def getDomValue(node: dom.Node): Option[String] = {
    node match {
      case el: dom.Element =>
        Option(getRawDomValue.applyOrElse(el, default = null))
      case _ =>
        None
    }
//
//    val propValue = node.asInstanceOf[js.Dynamic].selectDynamic(name)
//    val jsUndef = js.undefined
//
//    propValue.asInstanceOf[Any] match {
//      case str: String if str.length == 0 => None
//      case `jsUndef` => None
//      case null => None
//      case _ => Some(decode(propValue.asInstanceOf[DomV]))
//    }
  }
}

