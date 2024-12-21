/*
 * Copyright 2023, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion

enum class Version {

    JAVA {
        override fun of(version: Int) = Plugin<Project> { project ->
            project.tasks
                .withType(JavaCompile::class.java)
                .configureEach { javaCompile ->
                    javaCompile.sourceCompatibility = JavaLanguageVersion.of(version).toString()
                    javaCompile.targetCompatibility = JavaLanguageVersion.of(version).toString()
                }
        }
    };

    abstract fun of(version: Int): Plugin<Project>
}
