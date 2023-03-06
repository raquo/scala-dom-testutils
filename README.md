# Scala DOM Test Utils
[![Build status](https://github.com/raquo/domtestutils/actions/workflows/test.yml/badge.svg)](https://github.com/raquo/domtestutils/actions/workflows/test.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.raquo/domtestutils_sjs1_3.svg)](https://search.maven.org/artifact/com.raquo/domtestutils_sjs1_3)


_Scala DOM Test Utils_ provides a convenient, type-safe way to assert that a real Javascript DOM node matches a certain description using an extensible DSL.

    "com.raquo" %%% "domtestutils" % "<version>"  // Scala.js 1.13.0+ only

You can use _Scala DOM Test Utils_ either directly to make assertions, or you if you're writing a DOM construction / manipulation library, to power its own test utils package.



## Project Status

This project exists only to serve the needs of testing [Laminar](https://github.com/raquo/Laminar) and the basic needs of testing Laminar applications. Emphasis on _basic_. This is not going to be a full fledged test kit, nor are there any guarantees of documentation or stability. If you want a something more, you'll need to fork this and/or create your own. It's a very small project anyway.

**I am very unlikely to accept PRs on this project** – please talk to me before spending your time.



## Example Test

```scala
import com.raquo.laminar.api.L._

// Create a JS DOM node that you want to test (example shows optional Laminar syntax)
val jsDomNode: org.scalajs.dom.Node = div(
  rel := "yolo"
  span("Hello, "),
  p("bizzare ", a(href := "http://y2017.com", "2017"), span(" world")),
  hr
).ref 
 
// Mount the DOM node for testing
mount(jsDomNode, "optional clue to show on failure")
 
// Assert that the mounted node matches the provided description (this test will pass given the input above)
expectNode(
  div.of(
    rel is "yolo", // Ensure that rel attribute is "yolo". Note: assertions for properties and styles work similarly 
    span.of("Hello, "), // Ensure the this element contains just one text node: "Hello, "
    p.of(
      "bizzare ",
      a.of(
        href is "http://y2017.com",
        title.isEmpty, // Ensure that title attribute is not set
        "2017"
      ),
      span.of(" world")
    ),
    hr // Just check existence of element and tag name. Equivalent to `hr like ()` 
  )
)
```

The above example gets `div`, `rel`, `href`, etc. from Laminar, and uses implicit conversions from these Laminar values to DOM TestUtils classes like `ExpectedNode` and `TestableHtmlAttr`.See more usage examples and glue code in [Laminar tests](https://github.com/raquo/Laminar/tree/master/src/test/scala/com/raquo/laminar)

Laminar is not required to use Scala DOM TestUtils. You can integrate similarly with any other Scala.js UI library.



## Usage

Canonical usage is to `mount` one DOM node / tree (e.g. the output of your component) and then test it using the `expectNode` method.

Alternative is to call `expectNode(actualNode, expectedNode)`, for example if you only want to test a subtree of what you mounted.

If the mechanics of `MountOps` do not work for you, you can bypass `MountOps` altogether and just call `ExpectedNode.checkNode(actualNode)` directly to get a list of errors.

**With ScalaTest**: Your test suite should extend the `MountSpec` trait. Use `mount` and `expectNode` methods in your test code. You can call `unmount` and then `mount` again within one test if you want to test multiple unrelated nodes (e.g. different variations in a loop). See Laminar's test suite for an example.

**Without ScalaTest**: You could write a tiny adapter like `MountSpec` for your test framework, which would:
 
- Extend `MountOps` and provide `doAssert` / `doFail` implementations specific to your test framework
- Call `resetDOM` in the beginning of each test, and `clearDOM` at the end of each test.
- However, this project depends on ScalaTest, currently just for the `source.Position` macro, but in the future the integration could be deepened.
- So, long term, you might be better off forking this project if you want to use a different testing library.
- You could probably also forgo `MountOps` and other such files and drop down to calling `ExpectedNode.checkNode(actualNode)` to get a list of errors, and build your own test util around it.



## Versioning

There is no promise of any backwards compatibility in this particular project. I _roughly_ align versions with Scala DOM Types for my own convenience.



## My Related Projects

- [Laminar](https://github.com/raquo/Laminar) – Reactive UI library based on _Scala DOM Types_
- [Scala DOM Types](https://github.com/raquo/scala-dom-types) – Type definitions for all the HTML tags, attributes, properties, and styles, used by Laminar and a few other similar libraries



## Author

Nikita Gazarov – [@raquo](https://twitter.com/raquo)

License – MIT
