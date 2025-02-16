package com.raquo.domtestutils.fixtures

import com.raquo.domtestutils.codecs.Codec

trait SimpleKey {
  val name: String
}

class Prop[V, DomV](
  override val name: String,
  val codec: Codec[V, DomV]
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

class StyleProp[V](
  override val name: String
) extends SimpleKey

class CompositeHtmlKey(
  val name: String,
  val separator: String
)

class CompositeSvgKey(
  val name: String,
  val separator: String
)
