package com.raquo.domtestutils.fixtures

import com.raquo.domtestutils.codecs.Codec

trait SimpleKey {
  val name: String
}

abstract class Prop[V](
  override val name: String,
) extends SimpleKey {

  type DomV

  val codec: Codec[V, DomV]
}

class GlobalAttr[V](
  override val name: String,
  val codec: Codec[V, String]
) extends SimpleKey

class HtmlAttr[V](
  override val name: String,
  val codec: Codec[V, String]
) extends SimpleKey

class SvgAttr[V](
  override val name: String,
  val codec: Codec[V, String],
  val namespace: Option[String]
) extends SimpleKey

class MathMlAttr[V](
  override val name: String,
  val codec: Codec[V, String]
) extends SimpleKey

class StyleProp[V](
  override val name: String
) extends SimpleKey

class CompositeKey(
  val name: String,
  val separator: String
)
