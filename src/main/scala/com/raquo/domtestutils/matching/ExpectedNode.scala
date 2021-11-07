package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import com.raquo.domtypes.generic.builders.Tag
import org.scalajs.dom

import scala.collection.mutable

class ExpectedNode protected (
  val maybeTagName: Option[String] = None,
  val isTextNode: Boolean = false,
  val isComment: Boolean = false
) {

  import ExpectedNode._

  // @TODO[Integrity] Write tests for this test util; it's quite complicated.

  private val checksBuffer: mutable.Buffer[Check] = mutable.Buffer(
    checkNodeType
  )

  private val expectedChildrenBuffer: mutable.Buffer[ExpectedNode] = mutable.Buffer()

  val nodeType: String = (maybeTagName, isTextNode, isComment) match {
    case (Some(tagName), false, false) => s"Element[$tagName]"
    case (None, true, false) => "Text"
    case (None, false, true) => "Comment"
    case _ => "[ExpectedNode.nodeType: InvalidNode]"
  }

  def checks: List[Check] = checksBuffer.toList

  def expectedChildren: List[ExpectedNode] = expectedChildrenBuffer.toList

  def addCheck(check: Check): Unit = {
    checksBuffer.append(check)
  }

  def addExpectedChild(child: ExpectedNode): Unit = {
    expectedChildrenBuffer.append(child)
  }

  def of(rules: Rule*): ExpectedNode = {
    rules.foreach(rule => rule.applyTo(this))
    this
  }

  def like(rules: Rule*): ExpectedNode = {
    of(rules: _*)
  }

  def checkNodeType(actualNode: dom.Node): MaybeError = {
    val actualNodeType = actualNode match {
      case el: dom.Element => s"Element[${el.tagName.toLowerCase}]"
      case t: dom.Text => "Text"
      case c: dom.Comment => "Comment"
    }
    if (actualNodeType == nodeType) {
      None
    } else {
      Some(s"Node type mismatch: actual node is a ${repr(actualNodeType)}, expected a ${repr(nodeType)}")
    }
  }

  def checkNode(node: dom.Node, clue: String): ErrorList = {
    val errorsFromThisNode: ErrorList = checks
      .flatMap(check => check(node))
      .map(error => withClue(clue, error))

    val actualNumChildren = node.childNodes.length
    val expectedNumChildren = expectedChildren.length

    val childErrors = if (actualNumChildren != expectedNumChildren) {
      List(
        withClue(clue = clue, s"Child nodes length mismatch: actual ${repr(actualNumChildren)}, expected ${repr(expectedNumChildren)}"),
        withClue(clue = clue, s"- Detailed comparison:\n    actual - ${repr(nodeListToList(node.childNodes))},\n  expected - ${repr(expectedChildren)}")
      )
    } else {
      expectedChildren.zipWithIndex.flatMap {
        case (expectedChildElement, index) =>
          expectedChildElement.checkNode(
            node = node.childNodes(index),
            clue = s"$clue --- @$index"
          )
      }
    }

    errorsFromThisNode ++ childErrors
  }

  // @TODO[Convenience] The checks that we apply could also report what they're doing here
  override def toString: String = {
    (maybeTagName, isTextNode, isComment) match {
      case (Some(tagName), false, false) =>
        s"ExpectedNode[Element,tag=${repr(tagName)}]"
      case (None, true, false) =>
        s"ExpectedNode[Text]"
      case (None, false, true) =>
        s"ExpectedNode[Comment]"
      case _ =>
        throw new Exception("ExpectedNode.toString: inconsistent state")
    }
  }
}

object ExpectedNode {

  def element(tag: Tag[_]): ExpectedNode = new ExpectedNode(maybeTagName = Some(tag.name))

  def element(tagName: String): ExpectedNode = new ExpectedNode(maybeTagName = Some(tagName))

  def comment: ExpectedNode = new ExpectedNode(isComment = true)

  def textNode: ExpectedNode = new ExpectedNode(isTextNode = true)

  def withClue(clue: String, message: String): String = {
    s"[$clue]: $message"
  }

  def checkText(expectedText: String)(node: dom.Node): MaybeError = {
    if (node.nodeType != dom.Node.TEXT_NODE) {
      Some(s"Node type mismatch: actual ${repr(node.nodeType)}, expected ${repr(dom.Node.TEXT_NODE)} (Text Node)")
    } else if (node.textContent != expectedText) {
      Some(s"Text node textContent mismatch: actual ${repr(node.textContent)}, expected ${repr(expectedText)}")
    } else {
      None
    }
  }

  def checkCommentText(expectedText: String)(node: dom.Node): MaybeError = {
    node match {
      case comment: dom.Comment =>
        if (comment.textContent == expectedText) {
          None
        } else {
          Some(s"Comment node text mismatch: actual ${repr(comment.textContent)}, expected ${repr(expectedText)}")
        }
      case _ =>
        Some(s"Node type mismatch [checkCommentText]: actual node ${repr(node)}, expected an instance of dom.Comment")
    }
  }

  def nodeListToList(nodeList: dom.NodeList[dom.Node]): List[dom.Node] = {
    // @TODO[Polish] Move into JSUtils
    var result: List[dom.Node] = Nil
    var i = 0
    while (i < nodeList.length) {
      result = result :+ nodeList(i)
      i += 1
    }
    result
  }
}
