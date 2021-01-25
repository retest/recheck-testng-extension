# <a href="https://retest.dev"><img src="https://assets.retest.org/retest/ci/logos/recheck-screen.svg" width="300"/></a>

[![Build Status](https://github.com/retest/recheck-testng-extension/workflows/build/badge.svg)](https://github.com/retest/recheck-testng-extension/actions?query=workflow%3Abuild)
[![Latest recheck-testng-extension on Maven Central](https://maven-badges.herokuapp.com/maven-central/de.retest/recheck-testng-extension/badge.svg?style=flat)](https://mvnrepository.com/artifact/de.retest/recheck-testng-extension)
[![Latest recheck-testng-extension releases on JitPack](https://jitpack.io/v/de.retest/recheck-testng-extension.svg)](https://jitpack.io/#de.retest/recheck-testng-extension)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck-testng-extension/blob/main/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck-testng-extension/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/)

[TestNG](https://testng.org/doc/) Extension for [recheck](https://github.com/retest/recheck). Automatic set up and tear down of tests using recheck.

## Features

* Calls `startTest` on all `RecheckLifecycle` objects before each test.
* Calls `capTest` on all `RecheckLifecycle` objects after each test.
* Calls `cap` on all `RecheckLifecycle` objects after all tests.

## Advantages

The extension automatically calls `startTest`, `capTest` and `cap`. So it is no longer required to call those methods manually. This reduces boilerplate code and ensures the lifecycle within a test using recheck.

## Build tools

You can add ***recheck-testng-extension*** as an external dependency to your project. It is available via the [release-page](https://github.com/retest/recheck-testng-extension/releases) which allows you to include it into your favorite build tool or via [Maven central](https://mvnrepository.com/artifact/de.retest/recheck-testng-extension): [![Latest recheck-testng-extension on Maven Central](https://maven-badges.herokuapp.com/maven-central/de.retest/recheck-testng-extension/badge.svg?style=flat)](https://mvnrepository.com/artifact/de.retest/recheck-testng-extension)

For the current version, please refer to the release-page.

### Maven

```xml
<dependency>
	<groupId>de.retest</groupId>
	<artifactId>recheck-testng-extension</artifactId>
	<version>${LATEST_VERSION_FROM_ABOVE_LINK}</version>
</dependency>
```

### Gradle

```gradle
compile 'de.retest:recheck-testng-extension:${LATEST_VERSION_FROM_ABOVE_LINK}'
```

## Usage

Recheck TestNG extension uses [TestNG's listener](https://testng.org/doc/documentation-main.html#testng-listeners) mechanism. It can be used by adding `@Listeners(RecheckHook.class)` to your test class.

## License

This project is licensed under the [AGPL license](LICENSE).
