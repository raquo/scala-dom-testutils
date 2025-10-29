package com.raquo.domtestutils

import com.raquo.domtestutils.fixtures.StyleProp
import org.scalajs.dom
import org.scalajs.dom.CSSStyleDeclaration

import scala.scalajs.js
import scala.scalajs.js.|

class TestableStyleSpec extends UnitSpec {

  val backgroundColor = new StyleProp[String]("background-color")
  val zIndex = new StyleProp[Int | String]("z-index")

  def setStyle[V](el: dom.Element, style: StyleProp[V])(value: V): Unit = {
    el.asInstanceOf[js.Dynamic]
      .selectDynamic("style")
      .asInstanceOf[js.UndefOr[CSSStyleDeclaration]]
      .foreach { css =>
        css.setProperty(style.name, value.toString)
      }
  }

  it("background-color: string style") {
    val el = dom.document.createElement("div").asInstanceOf[dom.html.Div]

    print("------")
    (backgroundColor nodeStyleIs "") (el) shouldBe None
    (backgroundColor nodeStyleIs "red") (el) shouldBe Some(
      s"""|Style `background-color` is missing:
          |- Actual:   (style missing, or empty string)
          |- Expected: "red"
          |""".stripMargin
    )

    setStyle(el, backgroundColor)("green")

    (backgroundColor nodeStyleIs "green") (el) shouldBe None
    (backgroundColor nodeStyleIs "red") (el) shouldBe Some(
      s"""|Style `background-color` value is incorrect:
          |- Actual:   "green"
          |- Expected: "red"
          |""".stripMargin
    )
  }

  it("z-index: int style") {
    val el = dom.document.createElement("div").asInstanceOf[dom.html.Div]

    (zIndex nodeStyleIs "") (el) shouldBe None
    (zIndex nodeStyleIs "100") (el) shouldBe Some(
      s"""|Style `z-index` is missing:
          |- Actual:   (style missing, or empty string)
          |- Expected: "100"
          |""".stripMargin
    )

    setStyle(el, zIndex)(100)

    (zIndex nodeStyleIs "100") (el) shouldBe None
    (zIndex nodeStyleIs "200") (el) shouldBe Some(
      s"""|Style `z-index` value is incorrect:
          |- Actual:   "100"
          |- Expected: "200"
          |""".stripMargin
    )
  }
}

