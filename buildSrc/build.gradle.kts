plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    implementation("com.google.protobuf:protobuf-java:4.28.3")
}

kotlin {
    jvmToolchain(JavaLanguageVersion.of(11).asInt())
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.5"
    }

    generateProtoTasks {
        ofSourceSet("main")
    }
}
