package com.raquo.domtestutils.matching

trait Rule[N] {
  def applyTo(testNode: ExpectedNode[N]): Unit
}
