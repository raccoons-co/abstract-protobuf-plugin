/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

import co.raccoons.gradle.BuildWorkflow
import co.raccoons.gradle.checkstyle.CheckstyleConfiguration
import co.raccoons.gradle.checkstyle.CheckstyleReportFormat
import co.raccoons.gradle.jacoco.JacocoConfiguration
import co.raccoons.gradle.jacoco.JacocoReportFormat
import co.raccoons.gradle.java.Version
import co.raccoons.gradle.javadoc.JavadocConfiguration
import co.raccoons.gradle.javadoc.JavadocTag
import co.raccoons.gradle.repository.Repository

allprojects {
    BuildWorkflow.of(project)
        .setGroup("co.raccoons.protoc")
        .setVersion("0.0.9")
        .use(Repository.MAVEN_LOCAL)
        .use(Repository.MAVEN_CENTRAL)
        .use(Version.JAVA.of(11))
        .use(Configuration.checkstyle())
        .use(Configuration.jacoco())
        .use(Configuration.javadoc())
}

internal object Configuration {

    fun checkstyle(): CheckstyleConfiguration =
        CheckstyleConfiguration.newBuilder()
            .setVersion("10.12.4")
            .enable(CheckstyleReportFormat.HTML)
            .build()

    fun jacoco(): JacocoConfiguration =
        JacocoConfiguration.newBuilder()
            .enable(JacocoReportFormat.HTML)
            .enable(JacocoReportFormat.XML)
            .build()

    fun javadoc(): JavadocConfiguration =
        JavadocConfiguration.newBuilder()
            .addTag(JavadocTag("apiNote", "API Note"))
            .addTag(JavadocTag("implSpec", "Implementation Specification"))
            .addTag(JavadocTag("implNote", "Implementation Note"))
            .build()
}
