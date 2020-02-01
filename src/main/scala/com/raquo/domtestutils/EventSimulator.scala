package com.raquo.domtestutils

import org.scalajs.dom

import scala.scalajs.js

trait EventSimulator {

  // @TODO[API] Make a simple simulator package
  // @TODO This could be more compatible and more configurable
  // @TODO see if we can use https://github.com/Rich-Harris/simulant

  @deprecated("Not needed in jsdom anymore", "0.10")
  def simulateClick(target: dom.html.Element): Unit = {
//    val pointerOpts = new dom.PointerEventInit {
//      override val view: UndefOr[Window] = dom.window
//    }
//    pointerOpts.bubbles = true
//    pointerOpts.cancelable = true
//    pointerOpts.composed = false
//    val evt = new dom.Event("click", pointerOpts)
//    target.dispatchEvent(evt)

//    val evt = dom.document.createEvent("HTMLEvents")
//    evt.initEvent("click", canBubbleArg = true, cancelableArg = true)
//    target.dispatchEvent(evt)

    target.click()
  }

  def simulateScroll(target: dom.Node): Unit = {
    val scrollOpts = new dom.PointerEventInit {
      override val view: js.UndefOr[dom.Window] = dom.window
    }
    scrollOpts.bubbles = true
    scrollOpts.cancelable = true
    scrollOpts.composed = false
    val evt = new dom.Event("scroll", scrollOpts)
    target.dispatchEvent(evt)
  }
}
