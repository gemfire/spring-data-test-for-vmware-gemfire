/*
 * Copyright 2024 Broadcom. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
  id("java-library")
  alias(libs.plugins.lombok)
	id("gemfire-repo-artifact-publishing")
}

java {
  withJavadocJar()
  withSourcesJar()

  toolchain{ languageVersion.set(JavaLanguageVersion.of(8))}
}

tasks.named<Javadoc>("javadoc") {
  title = "Spring Test for VMware GemFire Java API Reference"
  isFailOnError = false
}

publishingDetails {
  artifactName.set("spring-test-gemfire-${getGemFireBaseVersion()}-2.7")
  longName.set("Spring Test Framework for VMware GemFire 10.1 and Spring Data 2.7")
  description.set("Spring Test Framework for VMware GemFire 10.1 and Spring Data 2.7")
}

dependencies {
  api(libs.multithreadedtc)
  api(libs.junit)
  api(libs.assertJ)
  api(libs.mockito)
  api(libs.lombok)
  api(libs.spring.test)

  compileOnly(libs.spring.data.gemfire)

  implementation(libs.annotation.api)


  compileOnly(libs.find.bugs)

  implementation(libs.logback)

  compileOnly(libs.spring.boot) {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
  }

  compileOnly(libs.bundles.gemfire)

  testImplementation(libs.bundles.gemfire)

  testImplementation(libs.spring.data.gemfire) {
    exclude("com.vmware.gemfire")
  }
}

repositories {
  val additionalMavenRepoURLs: String by project
  if (additionalMavenRepoURLs.isNotEmpty() && additionalMavenRepoURLs.isNotBlank()) {
    additionalMavenRepoURLs.split(",").forEach {
      project.repositories.maven {
        this.url = uri(it)
      }
    }
  }
  maven { url = uri("https://repo.spring.io/milestone") }
}

fun getGemFireBaseVersion(): String {
  val gemfireVersion: String by project
  val split = gemfireVersion.split(".")
  if (split.size < 2) {
    throw RuntimeException("gemfireVersion is malformed")
  }
  return "${split[0]}.${split[1]}"
}
