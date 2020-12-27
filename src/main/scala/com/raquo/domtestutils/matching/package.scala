package com.raquo.domtestutils

import org.scalajs.dom

package object matching {

  trait Rule {
    // Don't name this `apply`. It would compete with the public ExpectedNode.apply syntax.
    // This method is only used internally so a lame name is ok
    def applyTo(node: ExpectedNode): Unit
  }

  type MaybeError = Option[String]

  type ErrorList = List[String]

  type Check = dom.Node => MaybeError
}
