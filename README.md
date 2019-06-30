# sherpair-utils

Utility type classes for Scala

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")

## Contribution

### Source code style

We use [Scalastyle](http://www.scalastyle.org/) to style our code base.

In case of contribution and you are an IntelliJ user, the "**Scala style inspection**" checkbox has to flagged (_Settings -> Editor -> Inspections -> Scala -> Code Style_)

You can check your code while the compilation takes place or by running the command :

```shell
$ sbt scalastyle
```

### Source code formatting

We use [Scalafmt](https://scalameta.org/scalafmt/) to format our code base.

In case of contribution and you are an IntelliJ user, you should install the [scalafmt plugin](https://plugins.jetbrains.com/plugin/8236-scalafmt), select Scalafmt as **Formatter** and flag the checkbox "**Reformat on file save**" (_Settings -> Editor -> Code Style -> Scala).

You can format your code by using the _alt+shift+L_ shortcut


## Build

```shell
$ sbt clean compile
```

## Unit Tests

### Unit Test Build-only

```shell
$ sbt test:compile
```

### Running Unit Tests

```shell
$ sbt test
```
