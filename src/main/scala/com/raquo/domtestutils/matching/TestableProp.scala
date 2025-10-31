package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import org.scalajs.dom

import scala.scalajs.js

// @TODO Create EventPropOps

class TestableProp[V, DomV](
  val name: String,
  val decode: DomV => V
) {

  def is(expectedValue: V): Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodePropIs(maybeExpectedValue = Some(expectedValue)))
  }

  def isEmpty: Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodePropIs(maybeExpectedValue = None))
  }

  private[domtestutils] def nodePropIs(maybeExpectedValue: Option[V])(node: dom.Node): MaybeError = {
    val maybeActualValue = getProp(node)
    if (node.isInstanceOf[dom.html.Element]) {
      (maybeActualValue, maybeExpectedValue) match {

        case (Some(actualValue), Some(expectedValue)) =>
          if (actualValue == expectedValue) {
            None
          } else {
            Some(s"""|Prop `${name}` value is incorrect:
                     |- Actual:   ${repr(actualValue)}
                     |- Expected: ${repr(expectedValue)}
                     |""".stripMargin)
          }

        case (None, Some(expectedValue)) =>
          val rawActualValue = node.asInstanceOf[js.Dynamic].selectDynamic(name)
          Some(s"""|Prop `${name}` is empty or missing:
                   |- Actual (raw): ${repr(rawActualValue)}
                   |- Expected:     ${repr(expectedValue)}
                   |""".stripMargin)

        case (Some(actualValue), None) =>
          Some(s"""|Prop `${name}` should be empty or not present:
                   |- Actual:   ${repr(actualValue)}
                   |- Expected: (empty / not present)
                   |""".stripMargin)

        case (None, None) =>
          None
      }
    } else {
      Some(s"Unable to verify Prop `${name}` because node $node is not a DOM HTML Element (might be a text node or an MathML / SVG element??)")
    }
  }

  private[domtestutils] def getProp(node: dom.Node): Option[V] = {
    val propValue = node.asInstanceOf[js.Dynamic].selectDynamic(name)
    val jsUndef = js.undefined

    propValue.asInstanceOf[Any] match {
      case str: String if str.isEmpty => None
      case `jsUndef` => None
      case null => None
      case _ => Some(decode(propValue.asInstanceOf[DomV]))
    }
  }
}
