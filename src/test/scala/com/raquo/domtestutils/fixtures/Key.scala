package com.raquo.domtestutils.fixtures

import com.raquo.domtestutils.codecs.Codec

trait Key { val name: String }

class Prop[V, DomV] (override val name: String, val codec: Codec[V, DomV]) extends Key

class HtmlAttr[V] (override val name: String, val codec: Codec[V, String]) extends Key

class SvgAttr[V] (override val name: String, val codec: Codec[V, String], val namespace: Option[String]) extends Key

class StyleProp[V] (override val name: String) extends Key
