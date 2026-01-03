package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import org.scalajs.dom
import org.scalajs.dom.CSSStyleDeclaration

import scala.scalajs.js

class TestableStyleProp[V](val name: String) {

  def is(expectedValue: V): Rule = (testNode: ExpectedNode) => {
    // @TODO[Integrity] I hope this toString is ok. We don't use any fancy types for CSS anyway.
    testNode.addCheck(nodeStyleIs(expectedValue.toString))
  }

  def isEmpty: Rule = (testNode: ExpectedNode) => {
    // #TODO[Integrity] Not 100% sure that this is reliable, but as far as I can tell, should work with our `getStyle` funciton.
    testNode.addCheck(nodeStyleIs(""))
  }

  private[domtestutils] def nodeStyleIs(expectedValue: String)(node: dom.Node): MaybeError = {
    val actualValue = getStyle(node)


    if (actualValue == expectedValue) {
      None
    } else {
      if (actualValue.nonEmpty) {
        Some(
          s"""|Style `${name}` value is incorrect:
              |- Actual:   ${repr(actualValue)}
              |- Expected: ${repr(expectedValue)}
              |""".stripMargin
        )
      } else {
        Some(
          s"""|Style `${name}` is missing:
              |- Actual:   (style missing, or empty string)
              |- Expected: ${repr(expectedValue)}
              |""".stripMargin
        )
      }
    }
  }

  private[domtestutils] def getStyle(node: dom.Node): String = {
    // Sadly this is the best we can do because can't detect if SVGStylable is
    // Note: this returns an empty string even for styles that aren't set.
    // Not sure if we can do better...
    node.asInstanceOf[js.Dynamic]
      .selectDynamic("style")
      .asInstanceOf[js.UndefOr[CSSStyleDeclaration]]
      .flatMap { css =>
        css.getPropertyValue(name) // if not set, returns empty string
      }
      .toOption
      .getOrElse("")
  }

}
