# JavaCC Compiler Plugin for Gradle

[![Build Status](https://travis-ci.org/johnmartel/javaccPlugin.svg)](https://travis-ci.org/johnmartel/javaccPlugin) 
[![Coverage Status](https://img.shields.io/coveralls/johnmartel/javaccPlugin.svg)](https://coveralls.io/r/johnmartel/javaccPlugin)

Provides the ability to use [JavaCC](http://javacc.java.net/) via [Gradle](http://www.gradle.org/). If the 'java' plugin is also applied, JavaCompile tasks will depend upon the compileJavacc task.

## Installation

Simply grab the plugin from Maven Central:

Add the following lines to your `build.gradle` script:

Gradle 2.1+
```groovy
plugins {
  id "ca.coglinc.javacc" version "2.0.4"
}
```

Gradle <2.1
```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath group: 'ca.coglinc', name: 'javacc-gradle-plugin', version: '2.0.4'
    }
}
apply plugin: 'ca.coglinc.javacc'
```

## Building

To build, simply run the following command in the directory where you checked out the plugin source:

`./gradlew clean build`

## Usage

Place your JavaCC code into `src/main/javacc`.
The generated Java code will be  put under `./build/generated/javacc` and will be compiled as part of the Java compile.

You can configure the input/output directory by configuring the compileJavacc task:
```
compileJavacc {
    inputDirectory = file('src/main/javacc')
    outputDirectory = file(project.buildDir.absolutePath + '/generated/javacc')
}
```
Due to the nature of the JavaCC compiler and its use of the OUTPUT_DIRECTORY parameter, you should prefer using `inputDirectory` over `source` provided by SourceTask. To use include/exclude filters, please refer to the [SourceTask](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.SourceTask.html) documentation. By default, only `*.jj` files are included. 

You can configure commandline arguments passed to JavaCC by specifying `javaccArguments` map in compileJavacc:
```
compileJavacc {
    javaccArguments = [grammar_encoding: 'UTF-8', static: 'false']
}
```

### Eclipse

If you are using Eclipse and would like your gradle project to compile nicely in eclipse and have the generated code in the build path, you can simply add the generated path to the main sourceSet and add a dependency on `compileJavacc` to `eclipseClasspath`.
```java
main {
    java {
        srcDir compileJavacc.outputDirectory
    }
}
    
eclipseClasspath.dependsOn("compileJavacc")
```

## Compatibility

This plugin requires Java 6+.

It has been tested with Gradle 1.11+. Please let us know if you have had success with other versions of Gradle.

## Signature

The artifacts for this plugin are signed using the [PGP key](http://pgp.mit.edu:11371/pks/lookup?op=get&search=0x321163AE83A4068A) for `jonathan.martel@coglinc.ca`.

## Releasing

The following command can be used to release the project, upload to Maven Central and upload to Bintray:
```./gradlew -PreleaseVersion=[version] -PnextVersion=[snapshot version] -PscmUrl=https://github.com/johnmartel/javaccPlugin.git -PossrhUsername=[username] -PossrhPassword=[password] -PgpgPassphrase=[passphrase] -PbintrayUser=[username] -PbintrayApiKey=[apiKey] clean :release:release```

## Changelog

### 2.0.4
- Plugin now builds with Gradle 2.2.1
- Now publishes to Bintray using the latest version of com.jfrog.bintray plugin and syncs to Maven Central via this plugin

### 2.0.3
- CompileJavaccTask is now a [SourceTask](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.SourceTask.html) and supports include/exclude filters
- Can now configure the input/output directory

### 2.0.2

- Improved the build system
- Added acceptance tests
- Support the gradle wrapper (Issue #10)
- Support passing optional arguments to Javacc (Issue #11)
- Support multiproject builds (Issue #3)

### 2.0.1

Fixed the gradle-plugin attribute for the Bintray package version.

### 2.0.0

- Migrated to Gradle 2.0
- Plugin id changed to 'ca.coglinc.javacc'
- Plugin is now available via the [Gradle Plugins repository](http://plugins.gradle.org)

### 1.0.1

Updated JavaCC to 6.1.2.

### 1.0.0

Initial version with limited features. Simply generates JavaCC files to Java from a non-configurable location into a non-configurable location.
