package com.raquo.domtestutils

import com.raquo.domtestutils.matching.ExpectedNode
import org.scalajs.dom

/** Utilities for mounting / unmounting a single DOM node and running assertions on it.
  * This functionality can be used with DOM nodes created by any means, you don't need to use Scala DOM Builder.
  * This trait is agnostic of the testing frameworks. We have an adapter for ScalaTest, see MountSpec.
  */
trait MountOps[N] {

  // === On nullable variables ===
  // `container` and `rootNode` are nullable because if they were an Option it would be too easy to
  // forget to handle the `None` case when mapping or foreach-ing over them.
  // In test code, We'd rather have a null pointer exception than an assertion that you don't
  // realize isn't running because it's inside a None.foreach.

  /** Container element that will hold the root node as a child. Container is mounted as a child of <body> element */
  var containerNode: dom.Element = null // see comment ^^^

  /** Root node is the node that we test in `expectNode`. It is the only child of the `containerNode` element */
  def rootNode: dom.Node = Option(containerNode).map(_.firstChild).orNull // see comment ^^^

  val defaultMountedElementClue = "root"

  /** Prefix to add to error messages – useful to differentiate between different mount() calls within one test */
  var mountedElementClue: String = defaultMountedElementClue

  // @TODO[API] the errors printed out by these functions don't show the original line numbers. How can we fix that?

  /** If condition is false, fail the test with a given message
    * This method exists for compatibility with different test frameworks.
    */
  def doAssert(condition: Boolean, message: String): Unit

  /** Fail the test with a given message
    * This method exists for compatibility with different test frameworks.
    */
  def doFail(message: String): Nothing

  /** Check that the root node matches the provided description. Call doFail with an error message if the test fails. */
  def expectNode(expectedNode: ExpectedNode[N]): Unit = {
    rootNode match {
      case null =>
        doFail(s"ASSERT FAIL [expectNode]: Root node not found. Did you forget to mount()?")
      case _ =>
        val errors = expectedNode.checkNode(rootNode, clue = mountedElementClue)
        if (errors.nonEmpty) {
          doFail(s"Rendered element does not match expectations:\n${errors.mkString("\n")}")
        }
    }
  }

  /** Inject the root node into the DOM */
  def mount(node: dom.Node, clue: String): Unit = {
    assertEmptyContainer("mount")
    mountedElementClue = clue
    containerNode.appendChild(node)
  }

  /** Inject the root node into the DOM – with default clue
    * Note: [[defaultMountedElementClue]] should not be made a default value on the above `mount` method
    * because that prevents users from defining their own `mount` methods that accept default arguments
    * ("multiple overloaded alternatives of method mount define default arguments") error
    */
  def mount(node: dom.Node): Unit = mount(node, defaultMountedElementClue)

  /** Inject the root node into the DOM – alternative argument order for convenience */
  def mount(clue: String, node: dom.Node): Unit = mount(node, clue)

  /** Remove root node from the DOM */
  def unmount(): Unit = {
    assertRootNodeMounted("unmount")
    rootNode.parentNode.removeChild(rootNode)
    mountedElementClue = defaultMountedElementClue
  }

  /** Remove all traces of previous tests from the DOM: Unmount the root node and remove the container from the DOM */
  def clearDOM(): Unit = {
    if (rootNode != null) {
      unmount()
    }
    containerNode = null
    dom.document.body.textContent = "" // remove the container
  }

  /** Clear the DOM and create a new container. This should be called before each test. */
  def resetDOM(): Unit = {
    clearDOM()
    val newContainer = createContainer()
    dom.document.body.appendChild(newContainer)
    containerNode = newContainer
  }

  def createContainer(): dom.Element = {
    val container = dom.document.createElement("div")
    container.setAttribute("id", "app-container")
    container
  }

  def assertEmptyContainer(clue: String): Unit = {
    containerNode match {
      case null =>
        doFail(s"ASSERT FAIL [$clue]: Container not found. Usually, resetDocument() creates the container in withFixture().")
      case _ =>
        doAssert(
          containerNode.parentNode == dom.document.body,
          s"ASSERT FAIL [$clue]: Container is not mounted to <body> (what did you do!?)."
        )
        assert(
          containerNode.firstChild == null,
          s"ASSERT FAIL [$clue]: Unexpected children in container. Did you forget to unmount() the previous node?"
        )
        assert(
          rootNode == null,
          s"ASSERT FAIL [$clue]: Can not override non-null rootNode variable in mount(). Did you override unmount() method and forget to set rootNode = null?"
        )
    }
  }

  def assertRootNodeMounted(clue: String): Unit = {
    doAssert(
      rootNode != null,
      s"ASSERT FAIL [$clue]: There is no root node to unmount. Did you forget to mount()?"
    )
  }
}
