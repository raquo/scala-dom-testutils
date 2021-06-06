package com.raquo.domtestutils.mu

import com.raquo.domtestutils.{EventSimulator, Utils}
import org.scalajs.dom

/**
  * Sanity checks on the testing environment.
  * This does not use this library at all.
  */
class DomEnvSpec extends munit.FunSuite with EventSimulator with Utils {

  test("renders elements with attributes") {
    val spanId = randomString("spanId_")
    val span = dom.document.createElement("span")
    span.setAttribute("id", spanId)
    dom.document.body.appendChild(span)

    assertEquals(span.id, spanId)
  }

  test("handles click events") {
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
    assertEquals(callbackCount, 1)

    // Click event should bubble up
    span.click()
    assertEquals(callbackCount, 2)

    // Click should not be counted on unrelated div
    div2.click()
    assertEquals(callbackCount, 2)
  }
}

