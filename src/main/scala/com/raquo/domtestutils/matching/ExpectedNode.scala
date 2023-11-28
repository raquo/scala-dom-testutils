package com.raquo.domtestutils.matching

import com.raquo.domtestutils.Utils.repr
import org.scalajs.dom

import scala.collection.mutable

class ExpectedNode protected (
  val maybeTagName: Option[String] = None,
  val isTextNode: Boolean = false,
  val isComment: Boolean = false
) {

  import ExpectedNode._

  // @TODO[Integrity] Write tests for this test util; it's quite complicated.

  val nodeType: String = (maybeTagName, isTextNode, isComment) match {
    case (Some(tagName), false, false) => s"Element[$tagName]"
    case (None, true, false) => "Text"
    case (None, false, true) => "Comment"
    case _ => "[ExpectedNode.nodeType: InvalidNode]"
  }

  private val expectedChildrenBuffer: mutable.Buffer[ExpectedNode] = mutable.Buffer()

  private val checksBuffer: mutable.Buffer[Check] = mutable.Buffer(
    ExpectedNode.checkNodeType(nodeType)
  )

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

    errorsFromThisNode.distinct ++ childErrors
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

  def element(tagName: String): ExpectedNode = new ExpectedNode(maybeTagName = Some(tagName))

  def comment: ExpectedNode = new ExpectedNode(isComment = true)

  def textNode: ExpectedNode = new ExpectedNode(isTextNode = true)

  def withClue(clue: String, message: String): String = {
    s"[$clue]: $message"
  }

  def nodeType(node: dom.Node): String = {
    node match {
      case el: dom.Element => s"Element[${el.tagName.toLowerCase}]"
      case t: dom.Text => "Text"
      case c: dom.Comment => "Comment"
    }
  }

  def checkNodeType(expectedNodeType: String)(node: dom.Node): MaybeError = {
    val actualNodeType = nodeType(node)
    if (actualNodeType == expectedNodeType) {
      None
    } else {
      Some(s"Node type mismatch: actual node is a ${repr(actualNodeType)}, expected a ${repr(expectedNodeType)}")
    }
  }

  def checkText(expectedText: String)(node: dom.Node): MaybeError = {
    checkNodeType("Text")(node).orElse {
      if (node.textContent != expectedText) {
        Some(s"Text node textContent mismatch: actual ${repr(node.textContent)}, expected ${repr(expectedText)}")
      } else {
        None
      }
    }
  }

  def checkCommentText(expectedText: String)(node: dom.Node): MaybeError = {
    checkNodeType("Comment")(node).orElse {
      if (node.textContent != expectedText) {
        Some(s"Comment node text mismatch: actual ${repr(node.textContent)}, expected ${repr(expectedText)}")
      } else {
        None
      }
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
