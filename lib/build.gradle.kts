/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

import co.raccoons.gradle.BuildWorkflow
import co.raccoons.gradle.java.*
import co.raccoons.gradle.java.Manifest
import co.raccoons.gradle.publish.MavenPublishConfiguration
import co.raccoons.gradle.publish.maven.License
import co.raccoons.gradle.publish.maven.Pom
import co.raccoons.gradle.publish.maven.Publication
import co.raccoons.gradle.test.TestJUnitConfiguration
import java.time.LocalDateTime


plugins {
    java
    id("com.google.protobuf") version "0.9.4"
}

project.tasks.withType(Test::class.java){
    useJUnitPlatform()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.5"
    }

    generateProtoTasks {
        ofSourceSet("main")
    }

    plugins {

    }
}

BuildWorkflow.of(project)
    .use(Configuration.java())
    .use(Configuration.javaLibrary())
    .use(Configuration.testJUnit())
    .use(Configuration.mavenPublish())

internal object Configuration {

    fun java(): JavaConfiguration {
        val manifest = Manifest.newBuilder()
            .putAttributes("Name", "Abstract Protoc Plugin Library")
            .putAttributes("Implementation-Title", "co.raccoons.protoc")
            .putAttributes("Implementation-Vendor", "Raccoons")
            .putAttributes("Implementation-Build-Date", LocalDateTime.now().toString())
            .build();

        return JavaConfiguration(manifest)
    }

    fun javaLibrary(): JavaLibraryConfiguration =
        JavaLibraryConfiguration.newBuilder()
            .addDependency(Implementation("com.google.guava","guava","33.4.0-jre"))
            .addDependency(Implementation("com.google.protobuf","protobuf-java","4.28.3"))
            .build()

    fun testJUnit(): TestJUnitConfiguration =
        TestJUnitConfiguration.newBuilder()
            .addDependency(TestImplementation("org.junit.jupiter", "junit-jupiter","5.11.4"))
            .addDependency(TestImplementation("org.junit.jupiter","junit-jupiter-params","5.11.4"))
            .addDependency(TestImplementation("com.google.guava","guava-testlib","33.4.0-jre"))
            .addDependency(TestImplementation("com.google.truth", "truth", "1.4.4"))
            .build()



    fun mavenPublish(): MavenPublishConfiguration {
        val license =
            License.newBuilder()
                .setName("ProtocExtra")
                .setUrl("https://opensource.org/license/mit")
                .build()

        val pom =
            Pom.newBuilder()
                .setName("ProtocExtra")
                .setDescription("Abstract Protoc Plugin Library")
                .setUrl("https://github.com/raccoons-co/ProtocExtra")
                .setLicense(license)
                .build()

        val publication =
            Publication.newBuilder()
                .setArtifactId("protoc-extra-lib")
                .setPom(pom)
                .build()

        return MavenPublishConfiguration(publication)
    }
}

configure<SourceSetContainer> {
    this.named("main").configure {
        this.java.srcDirs(project.layout.projectDirectory.dir("../generated/"))
    }
}
