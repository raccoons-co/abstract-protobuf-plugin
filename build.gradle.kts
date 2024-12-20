import co.raccoons.gradle.BuildWorkflow
import co.raccoons.gradle.checkstyle.CheckstyleConfiguration
import co.raccoons.gradle.checkstyle.CheckstyleReportFormat
import co.raccoons.gradle.jacoco.JacocoConfiguration
import co.raccoons.gradle.jacoco.JacocoReportFormat
import co.raccoons.gradle.java.*
import co.raccoons.gradle.java.Manifest
import co.raccoons.gradle.javadoc.JavadocConfiguration
import co.raccoons.gradle.javadoc.JavadocTag
import co.raccoons.gradle.publish.MavenPublishConfiguration
import co.raccoons.gradle.publish.maven.License
import co.raccoons.gradle.publish.maven.Pom
import co.raccoons.gradle.publish.maven.Publication
import co.raccoons.gradle.repository.Repository
import co.raccoons.gradle.test.TestNgConfiguration
import java.time.LocalDateTime

plugins {
    java
    id("com.google.protobuf") version "0.9.4"
}

configure<SourceSetContainer> {
    this.named("main").configure {
        this.java.srcDirs(project.layout.projectDirectory.dir("generated/"))
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.5"
    }

    generateProtoTasks {
        ofSourceSet("main")
    }
}

/**
 * Gradle build entry point.
 */
BuildWorkflow.of(project)
    .setGroup("co.raccoons")
    .setVersion("0.0.7")
    .use(Repository.MAVEN_LOCAL)
    .use(Repository.MAVEN_CENTRAL)
    .use(Configuration.java())
    .use(JavaLibraryConfiguration.newBuilder()
        .addDependency(Implementation("com.google.guava","guava","33.3.1-jre"))
        .addDependency(Implementation("com.google.protobuf","protobuf-java","4.28.3"))
        .build())
    .use(Version.JAVA.of(11))
    .use(Configuration.testNG())
    .use(Configuration.jacoco())
    .use(Configuration.javadoc())
    .use(Configuration.checkstyle())
    .use(Configuration.mavenPublish())


/**
 * The configuration of the project plugins.
 */
internal object Configuration {

    /** Returns ready to use Java plugin configuration. */
    fun java(): JavaConfiguration {
        val manifest = Manifest.newBuilder()
            .putAttributes("Name", "Abstract Protoc Plugin Library")
            .putAttributes("Implementation-Title", "co.raccoons.protoc")
            .putAttributes("Implementation-Vendor", "Raccoons")
            .putAttributes("Implementation-Build-Date", LocalDateTime.now().toString())
            .build();

        return JavaConfiguration(manifest)
    }

    /** Returns ready to use TestNG plugin configuration. */
    fun testNG(): TestNgConfiguration =
        TestNgConfiguration.newBuilder()
            .addDependency(TestImplementation("com.google.truth", "truth", "1.4.4"))
            .addDependency(TestImplementation("org.testng", "testng", "7.8.0"))
            .addDependency(TestImplementation("org.slf4j", "slf4j-simple", "2.0.7"))
            .build()

    /** Returns ready to use Jacoco plugin configuration. */
    fun jacoco(): JacocoConfiguration =
        JacocoConfiguration.newBuilder()
            .enable(JacocoReportFormat.HTML)
            .enable(JacocoReportFormat.XML)
            .build()

    /** Returns ready to use Javadoc plugin configuration. */
    fun javadoc(): JavadocConfiguration =
        JavadocConfiguration.newBuilder()
            .addTag(JavadocTag("apiNote", "API Note"))
            .addTag(JavadocTag("implSpec", "Implementation Specification"))
            .addTag(JavadocTag("implNote", "Implementation Note"))
            .build()

    /** Returns ready to use Checkstyle plugin configuration. */
    fun checkstyle(): CheckstyleConfiguration =
        CheckstyleConfiguration.newBuilder()
            .setVersion("10.12.2")
            .enable(CheckstyleReportFormat.HTML)
            .build()

    /** Returns ready to use Maven publish plugin configuration. */
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
                .setArtifactId("ProtocExtra")
                .setPom(pom)
                .build()

        return MavenPublishConfiguration(publication)
    }
}
