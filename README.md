# Scala DOM Test Utils

This project provides a convenient way to assert that a real Javascript DOM node matches a certain description using an extensible DSL.

## Usage

```scala
// Create a DOM node using Scala DOM Builder (OPTIONAL) and mount it for testing
mount(
  div(
    rel := "yolo"
    span("Hello, "),
    p("bizzare", a(href := "http://2017.com", " 2017 "), span("world")),
    hr
  )
)

// Assert that the mounted node matches the provided description (this test will pass given the input above)
expectNode(div like(
  rel is "yolo", // Ensure that rel attribute is "yolo". Note: assertions for properties work similarly 
  span like "Hello, ", // Ensure the this element contains just one text node: "Hello, "
  p like (
    "bizzare",
    a like (
      href is "http://2017.com",
      title isEmpty, // Ensure that title attribute is not set
      " 2017 "
    ),
    span like "world"
  ),
  hr likeWhatever // Ensure the existence of this hr element. We only check the tag name. Basically equivalent to `hr like ()` 
))
```

[Scala DOM Builder](https://github.com/raquo/scala-dom-builder) is a minimal, unopinionated library for building and manipulating Javascript DOM trees.

See more usage examples in [Scala DOM Builder tests](https://github.com/raquo/scala-dom-builder/tree/master/js/src/test/scala/com/raquo/dombuilder/dombuilder) and [Laminar tests](https://github.com/raquo/laminar/tree/master/src/test/scala/com/raquo/laminar)

### Usage Without Scala DOM Builder

You do NOT need to use Scala DOM Builder to use our Test Utils. You can test DOM nodes created by any other means. However, this test framework uses Scala DOM Builder functionality to mount DOM nodes, so you will need to implement the `mount` / `unmount` logic yourself. Feel free to get in touch if you need help with that, or if the existing API is constricting your use case. 

### TODO

* Explain in more detail how to make assertions
* Explain the basics of Scala DOM Builder node / attr / etc. types
* Explain how to create your own custom rules
* Document available assertions

## Author

Nikita Gazarov – [raquo.com](http://raquo.com)

License – MIT
