package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import com.raquo.domtypes.generic.keys.Prop
import org.scalajs.dom

import scala.scalajs.js

// @TODO Create EventPropOps

class TestableProp[V, DomV](val prop: Prop[V, DomV]) extends AnyVal {

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
        case (None, None) => None
        case (None, Some(expectedValue)) =>
          Some(s"Prop `${prop.name}` is empty or missing, expected ${repr(expectedValue)}")
        case (Some(actualValue), None) =>
          Some(s"Prop `${prop.name}` should be empty / not present: actual value ${repr(actualValue)}, expected to be missing")
        case (Some(actualValue), Some(expectedValue)) =>
          if (actualValue != expectedValue) {
            Some(s"Prop `${prop.name}` value is incorrect: actual value ${repr(actualValue)}, expected value ${repr(expectedValue)}")
          } else {
            None
          }
      }
    } else {
      Some(s"Unable to verify Prop `${prop.name}` because node $node is not a DOM HTML Element (might be a text node?)")
    }
  }

  private[domtestutils] def getProp(node: dom.Node): Option[V] = {
    val propValue = node.asInstanceOf[js.Dynamic].selectDynamic(prop.name)
    val jsUndef = js.undefined

    propValue.asInstanceOf[Any] match {
      case str: String if str.length == 0 => None
      case `jsUndef` => None
      case null => None
      case _ => Some(prop.codec.decode(propValue.asInstanceOf[DomV]))
    }
  }
}
