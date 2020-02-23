# Alarm Tiles

[![Build Status](https://travis-ci.com/linusfessler/alarm-tiles.svg?branch=master)](https://travis-ci.com/linusfessler/alarm-tiles)
[![Coverage Status](https://coveralls.io/repos/github/linusfessler/alarm-tiles/badge.svg?branch=master)](https://coveralls.io/github/linusfessler/alarm-tiles?branch=master)
[![License](https://img.shields.io/github/license/linusfessler/alarm-tiles)](LICENSE)

Alarm Tiles is an Android app that provides quick settings tiles (sleep timer, alarm, alarm timer, stopwatch)
that allow the user to effortlessly enable an alarm or sleep timer or use a stopwatch without leaving the context of their current app.

## Main technologies and libraries
* [Kotlin](https://kotlinlang.org)
* [RxJava](https://github.com/ReactiveX/RxJava) + [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Mobius](https://github.com/spotify/mobius)
* [Room](https://developer.android.com/topic/libraries/architecture/room)
* [Dagger](https://github.com/google/dagger)
* [Navigation](https://developer.android.com/guide/navigation)
* [Material Components for Android](https://github.com/material-components/material-components-android)

## Translations
Currently translated in English (default) and German.
If you want to contribute to the translation, add/edit the strings.xml in the app/src/main/res/values folder corresponding to the language.

## CI
This project uses [Travis CI](https://travis-ci.com) to lint and test, and to send the coverage reports to Coveralls.
Unit tests are run with
[JUnit 5](https://github.com/junit-team/junit5)
([Android JUnit 5 Gradle plugin](https://github.com/mannodermaus/android-junit5)).
After the test stage, it uses
[Jacoco](https://github.com/jacoco/jacoco)
to report the test coverage and send it to
[Coveralls](https://coveralls.io)
([Coveralls Gradle plugin](https://github.com/kt3k/coveralls-gradle-plugin)).
