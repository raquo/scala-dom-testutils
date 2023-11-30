package com.raquo.domtestutils

import com.raquo.domtestutils.matching.ExpectedNode
import org.scalactic
import org.scalajs.dom

/** Utilities for mounting / unmounting a single DOM node and running assertions on it.
  * This functionality can be used with DOM nodes created by any means, you don't need to use Scala DOM Builder.
  * This trait is (almost) agnostic of the testing frameworks. We have an adapter for ScalaTest, see MountSpec.
  */
trait MountOps {

  // === On nullable variables ===
  // `container` and `rootNode` are nullable because if they were an Option it would be too easy to
  // forget to handle the `None` case when mapping or foreach-ing over them.
  // In test code, We'd rather have a null pointer exception than an assertion that you don't
  // realize isn't running because it's inside a None.foreach.

  /** Container element that will hold the root node as a child. Container is mounted as a child of <body> element */
  var containerNode: dom.Element = null // see "On nullable variables" comment above ^^^

  /** Root node is the node that we test in `expectNode`. It is the only child of the `containerNode` element */
  def rootNode: dom.Node = Option(containerNode).map(_.firstChild).orNull // see "On nullable variables" comment above ^^^

  val defaultMountedElementClue = "root"

  /** Prefix to add to error messages – useful to differentiate between different mount() calls within one test */
  var mountedElementClue: String = defaultMountedElementClue

  // @TODO[API] the errors printed out by these functions don't show the original line numbers. How can we fix that?

  /** If condition is false, fail the test with a given message
    * This method exists for compatibility with different test frameworks.
    */
  def doAssert(
    condition: Boolean,
    message: String
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position,
  ): Unit

  /** Fail the test with a given message
    * This method exists for compatibility with different test frameworks.
    */
  def doFail(message: String)(implicit pos: scalactic.source.Position): Nothing

  /** Check that the root node matches the provided description. Call doFail with an error message if the test fails. */
  def expectNode(expectedNode: ExpectedNode)(implicit pos: scalactic.source.Position): Unit = {
    rootNode match {
      case null =>
        doFail(s"ASSERT FAIL [expectNode]: Root node not found. Did you forget to mount()?")(pos)
      case _ =>
        val errors = expectedNode.checkNode(rootNode, clue = mountedElementClue)
        if (errors.nonEmpty) {
          doFail(s"Rendered element does not match expectations:\n${errors.mkString("\n")}")(pos)
        }
    }
  }

  /** Check that a given node matches the provided description. Call doFail with an error message if the test fails. */
  def expectNode(
    actualNode: dom.Node,
    expectedNode: ExpectedNode,
    clue: String = mountedElementClue
  )(
    implicit pos: scalactic.source.Position
  ): Unit = {
    val errors = expectedNode.checkNode(actualNode, clue)
    if (errors.nonEmpty) {
      doFail(s"Given node does not match expectations:\n${errors.mkString("\n")}")(pos)
    }
  }

  /** Inject the root node into the DOM – with default clue
    * Note: [[defaultMountedElementClue]] should not be made a default value on the above `mount` method
    * because that prevents users from defining their own `mount` methods that accept default arguments
    * ("multiple overloaded alternatives of method mount define default arguments") error
    */
  def mount(
    node: dom.Node
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position
  ): Unit = {
    mount(node, defaultMountedElementClue)(prettifier, pos)
  }

  /** Inject the root node into the DOM */
  def mount(
    node: dom.Node,
    clue: String
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position
  ): Unit = {
    assertEmptyContainer("mount")(prettifier, pos)
    mountedElementClue = clue
    containerNode.appendChild(node)
  }

  /** Inject the root node into the DOM – alternative argument order for convenience */
  def mount(
    clue: String,
    node: dom.Node
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position
  ): Unit = {
    mount(node, clue)(prettifier, pos)
  }

  /** Remove root node from the DOM */
  def unmount(
    clue: String = "unmount"
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position
  ): Unit = {
    assertRootNodeMounted("unmount:" + clue)(prettifier, pos)
    rootNode.parentNode.removeChild(rootNode)
    mountedElementClue = defaultMountedElementClue
  }

  /** Remove all traces of previous tests from the DOM: Unmount the root node and remove the container from the DOM */
  def clearDOM(
    clue: String = "clearDOM"
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position
  ): Unit = {
    if (rootNode != null) {
      unmount("clearDOM:" + clue)(prettifier, pos)
    }
    containerNode = null
    dom.document.body.textContent = "" // remove the container
  }

  /** Clear the DOM and create a new container. This should be called before each test. */
  def resetDOM(
    clue: String = "resetDOM"
  )(
    implicit prettifier: scalactic.Prettifier,
    pos: scalactic.source.Position
  ): Unit = {
    clearDOM("resetDOM:" + clue)(prettifier, pos)
    val newContainer = createContainer()
    dom.document.body.appendChild(newContainer)
    containerNode = newContainer
  }

  def createContainer(): dom.Element = {
    val container = dom.document.createElement("div")
    container.setAttribute("id", "app-container")
    container
  }

  def assertEmptyContainer(clue: String)(implicit prettifier: scalactic.Prettifier, pos: scalactic.source.Position): Unit = {
    containerNode match {
      case null =>
        doFail(s"ASSERT FAIL [$clue]: Container not found. Usually, resetDOM() creates the container in withFixture().")(pos)
      case _ =>
        doAssert(
          containerNode.parentNode == dom.document.body,
          s"ASSERT FAIL [$clue]: Container is not mounted to <body> (what did you do!?)."
        )(prettifier, pos)
        doAssert(
          containerNode.firstChild == null,
          s"ASSERT FAIL [$clue]: Unexpected children in container. Did you forget to unmount() the previous node?"
        )(prettifier, pos)
        doAssert(
          rootNode == null,
          s"ASSERT FAIL [$clue]: Can not override non-null rootNode variable in mount(). Did you override unmount() method and forget to set rootNode = null?"
        )(prettifier, pos)
    }
  }

  def assertRootNodeMounted(clue: String)(implicit prettifier: scalactic.Prettifier, pos: scalactic.source.Position): Unit = {
    doAssert(
      rootNode != null,
      s"ASSERT FAIL [$clue]: There is no root node to unmount. Did you forget to mount()?"
    )(prettifier, pos)
  }
}
