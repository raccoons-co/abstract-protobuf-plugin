import co.raccoons.gradle.BuildWorkflow
import co.raccoons.gradle.java.Implementation
import co.raccoons.gradle.java.JavaConfiguration
import co.raccoons.gradle.java.JavaLibraryConfiguration
import co.raccoons.gradle.java.Manifest
import co.raccoons.gradle.publish.MavenPublishConfiguration
import co.raccoons.gradle.publish.maven.License
import co.raccoons.gradle.publish.maven.Pom
import co.raccoons.gradle.publish.maven.Publication
import java.time.LocalDateTime

plugins {
    id("com.google.protobuf") version "0.9.4"
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.5"
    }

    generateProtoTasks {
        ofSourceSet("main")
    }
}

BuildWorkflow.of(project)
    .use(Configuration.java())
    .use(Configuration.javaLibrary())
    .use(Configuration.mavenPublish())

internal object Configuration {

    fun java(): JavaConfiguration {
        val manifest = Manifest.newBuilder()
            .putAttributes("Name", "Protoc Plugin")
            .putAttributes("Implementation-Title", "co.raccoons.protoc")
            .putAttributes("Implementation-Vendor", "Raccoons")
            .putAttributes("Implementation-Build-Date", LocalDateTime.now().toString())
            .build();
        return JavaConfiguration(manifest)
    }

    fun javaLibrary(): JavaLibraryConfiguration =
        JavaLibraryConfiguration.newBuilder()
            .addDependency(Implementation("co.raccoons.protoc", "protoc-extra-lib", "0.0.8"))
            .addDependency(Implementation("com.google.guava","guava","33.4.0-jre"))
            .addDependency(Implementation("com.google.protobuf", "protobuf-java", "4.28.3"))
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
                .setDescription("Protoc Plugin")
                .setUrl("https://github.com/raccoons-co/ProtocExtra")
                .setLicense(license)
                .build()

        val publication =
            Publication.newBuilder()
                .setArtifactId("protoc-extra-plugin")
                .setPom(pom)
                .build()

        return MavenPublishConfiguration(publication)
    }
}
