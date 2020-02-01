package com.raquo.domtestutils.scalatest

import com.raquo.domtestutils.{EventSimulator, Utils}
import org.scalajs.dom
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/**
  * Sanity checks on the testing environment.
  * This does not use this library at all.
  */
class DomEnvSpec extends AnyFunSpec with Matchers with EventSimulator with Utils {

  it("renders elements with attributes") {
    val spanId = randomString("spanId_")
    val span = dom.document.createElement("span")
    span.setAttribute("id", spanId)
    dom.document.body.appendChild(span)

    span.id shouldBe spanId
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
    callbackCount shouldBe 1

    // Click event should bubble up
    span.click()
    callbackCount shouldBe 2

    // Click should not be counted on unrelated div
    div2.click()
    callbackCount shouldBe 2
  }
}

