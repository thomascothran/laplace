# 📈 Laplace: A Project Projection Tool

> "The most important questions in life are, indeed, for the most part really only questions of probability." - Simone Pierre Laplace

Laplace is a tool for planning service delivery. It answers questions like:

- :rocket: How much can we do in a given time period?
- :watch: How long will a deliverable take?
- :construction_worker: How best to assign capacity across different initiatives?
- :dollar: What is the expected economic benefit of our project planning?

Laplace makes it easy to use a number of methods to illuminate planning of projects.

## 👩‍💼 What can I use this for?

Currently, Laplace can be used to answer the following questions:

#### :calendar: How long will a group of tasks take?

If you have a group of tasks (say, for an Epic or Feature), you can get range for how long those tasks might take.

Laplace uses throughput to determine the rate at which you can expect tasks to be complete. Ideally, this rate comes from data in your ticketing system. But you can also experiment with your own estimates if you do not have historical data, or if you want to experiment

#### 🎟️ How many tasks can be completed in a certain time period?

Suppose you want to plan for the next sprint, and you want to know how many work items can be expected to be completed in that time frame. Laplace will use your historical data to answer this question with varying confidence intervals.

#### :ticket: For a particular work item, when can I expect it to be complete?

Suppose that your tickets move through the following stages

## 📑 Techniques

Current techniques:

- Monte Carlo

Planned:

- Cost of Delay
- Reference class forecasting
- Economic Modeling

## 🔢 Data Sources

TODO: add the ability to import historical data from Jira and other ticketing systems

## 👩🏽‍ Usage

Laplace can currently be used as a library. Usage as a standalone service with a web interface is in progress.

### As a Library

Laplace may be used with Clojure's Tools Deps, via get dependencies.

### As a Service

## Installation

Download from https://github.com/thomascothran/laplace

## Development

FIXME: explanation

Run the project directly, via `:exec-fn`:

    $ clojure -X:run-x
    Hello, Clojure!

Run the project, overriding the name to be greeted:

    $ clojure -X:run-x :name '"Someone"'
    Hello, Someone!

Run the project directly, via `:main-opts` (`-m thomascothran.laplace`):

    $ clojure -M:run-m
    Hello, World!

Run the project, overriding the name to be greeted:

    $ clojure -M:run-m Via-Main
    Hello, Via-Main!

Run the project's tests (they'll fail until you edit them):

    $ clojure -T:build test

Run the project's CI pipeline and build an uberjar (this will fail until you edit the tests to pass):

    $ clojure -T:build ci

This will produce an updated `pom.xml` file with synchronized dependencies inside the `META-INF`
directory inside `target/classes` and the uberjar in `target`. You can update the version (and SCM tag)
information in generated `pom.xml` by updating `build.clj`.

If you don't want the `pom.xml` file in your project, you can remove it. The `ci` task will
still generate a minimal `pom.xml` as part of the `uber` task, unless you remove `version`
from `build.clj`.

Run that uberjar:

    $ java -jar target/laplace-0.1.0-SNAPSHOT.jar

If you remove `version` from `build.clj`, the uberjar will become `target/laplace-standalone.jar`.

## License

Copyright © 2023 Thomas Cothran

Distributed under the Eclipse Public License version 1.0.
