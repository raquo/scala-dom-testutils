package com.raquo.domtestutils.scalatest

import com.raquo.domtestutils.{EventSimulator, Utils}
import org.scalajs.dom
import org.scalatest.funspec.AnyFunSpec

/**
  * Sanity checks on the testing environment.
  * This does not use this library at all.
  */
class DomEnvSpec extends AnyFunSpec with EventSimulator with Utils {

  it("renders elements with attributes") {
    val spanId = randomString("spanId_")
    val span = dom.document.createElement("span")
    span.setAttribute("id", spanId)
    dom.document.body.appendChild(span)

    assertResult(expected = spanId)(actual = span.id)
  }

  it("handles click events") {
    var callbackCount = 0

    def testEvent(ev: dom.MouseEvent): Unit = {
      callbackCount += 1
    }

    val div = dom.document.createElement("div").asInstanceOf[dom.html.Div]
    val div2 = dom.document.createElement("div").asInstanceOf[dom.html.Div]
    val span = dom.document.createElement("span").asInstanceOf[dom.html.Span]

    div.addEventListener[dom.MouseEvent]("click", testEvent _)

    div.appendChild(span)
    dom.document.body.appendChild(div)
    dom.document.body.appendChild(div2)

    // Direct hit
    div.click()
    assertResult(expected = 1)(actual = callbackCount)

    // Click event should bubble up
    span.click()
    assertResult(expected = 2)(actual = callbackCount)

    // Click should not be counted on unrelated div
    div2.click()
    assertResult(expected = 2)(actual = callbackCount)
  }
}

