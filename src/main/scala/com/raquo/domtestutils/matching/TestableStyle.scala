package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import com.raquo.domtypes.generic.keys.Style
import org.scalajs.dom
import org.scalajs.dom.CSSStyleDeclaration

import scala.scalajs.js
import scala.scalajs.js.|

class TestableStyle[V](val style: Style[V]) extends AnyVal {

  def is(expectedValue: V | String): Rule = (testNode: ExpectedNode) => {
    // @TODO[Integrity] I hope this toString is ok. We don't use any fancy types for CSS anyway.
    testNode.addCheck(nodeStyleIs(expectedValue.toString))
  }

  private[domtestutils] def nodeStyleIs(expectedValue: String)(node: dom.Node): MaybeError = {
    val actualValue = getStyle(node)


    if (actualValue == expectedValue) {
      None
    } else {
      if (actualValue.nonEmpty) {
        Some(
          s"""|Style `${style.name}` value is incorrect:
              |- Actual:   ${repr(actualValue)}
              |- Expected: ${repr(expectedValue)}
              |""".stripMargin
        )
      } else {
        Some(
          s"""|Style `${style.name}` is missing:
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
        css.getPropertyValue(style.name)
      }
      .toOption
      .getOrElse("")
  }

}
