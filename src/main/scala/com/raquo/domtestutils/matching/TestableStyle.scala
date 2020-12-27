package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import com.raquo.domtypes.generic.keys.Style
import org.scalajs.dom

import scala.scalajs.js

class TestableStyle[V](val style: Style[V]) extends AnyVal {

  def is(expectedValue: V): Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodeStyleIs(expectedValue))
  }

  private[domtestutils] def nodeStyleIs(expectedValue: V)(node: dom.Node): MaybeError = {
    val maybeActualValue = getStyle(node)
    maybeActualValue match {

      case Some(actualValue) =>
        if (actualValue == expectedValue) {
          None
        } else {
          Some(s"""|Style `${style.name}` value is incorrect:
                   |- Actual:   ${repr(actualValue)}
                   |- Expected: ${repr(expectedValue)}
                   |""".stripMargin)
        }

      case None =>
        Some(s"""|Style `${style.name}` is completely missing:
                 |- Actual:   (style missing)
                 |- Expected: ${repr(expectedValue)}
                 |""".stripMargin)
    }
  }

  private[domtestutils] def getStyle(node: dom.Node): Option[V] = {
    // @TODO[Integrity] Pattern match to dom.html.Element instead, and get style from there
    node.asInstanceOf[js.Dynamic]
      .selectDynamic("style")
      .selectDynamic(style.name)
      .asInstanceOf[js.UndefOr[V]].toOption
  }
}
