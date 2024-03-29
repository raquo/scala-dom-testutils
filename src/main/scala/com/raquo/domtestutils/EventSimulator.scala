package com.raquo.domtestutils

import org.scalajs.dom
import org.scalajs.dom.WheelEventInit

import scala.scalajs.js

trait EventSimulator {

  // @TODO[API] Make a simple simulator package
  // @TODO This could be more compatible and more configurable
  // @TODO see if we can use https://github.com/Rich-Harris/simulant

  @deprecated("Not needed in jsdom anymore", "0.10")
  def simulateClick(target: dom.html.Element): Unit = {
//    val evt = dom.document.createEvent("HTMLEvents")
//    evt.initEvent("click", canBubbleArg = true, cancelableArg = true)
//    target.dispatchEvent(evt)

    target.click()
  }

  // SVG elements don't have a click() method, so...
  def simulateClick(target: dom.svg.Element): Unit = {
    simulatePointerEvent("click", target)
  }

  // Text nodes don't have a click() method, so...
  def simulateClick(target: dom.Text): Unit = {
    simulatePointerEvent("click", target)
  }

  def simulateScroll(target: dom.Node): Unit = {
    val scrollOpts = new WheelEventInit {
//      override val view: js.UndefOr[dom.Window] = dom.window  // #TODO scalajs-dom v2.1.0 made this impossible, what now?
    }
    scrollOpts.bubbles = true
    scrollOpts.cancelable = true
    scrollOpts.composed = false
    val evt = new dom.Event("scroll", scrollOpts)
    target.dispatchEvent(evt)
  }

  /** @param eventType e.g. "click" */
  private def simulatePointerEvent(eventType: String, target: dom.Node): Unit = {
    val pointerOpts = new dom.PointerEventInit {
//      override val view: js.UndefOr[dom.Window] = dom.window  // #TODO scalajs-dom v2.1.0 made this impossible, what now?
    }
    pointerOpts.bubbles = true
    pointerOpts.cancelable = true
    pointerOpts.composed = false
    val evt = new dom.Event(eventType, pointerOpts)
    target.dispatchEvent(evt)
  }
}
