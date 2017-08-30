package com.raquo.domtestutils

// @TODO[SERVER]
import org.scalajs.dom

package object matching {

  type MaybeError = Option[String]

  type ErrorList = List[String]

  type Check = dom.Node => MaybeError
}
