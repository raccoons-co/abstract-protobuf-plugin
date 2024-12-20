/*
 * Copyright 2023, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.repository

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Dependency lookup repository.
 */
enum class Repository : Plugin<Project> {

    MAVEN_CENTRAL {
        /**@inheritDoc */
        override fun apply(project: Project) {
            project.repositories.mavenCentral()
        }
    },

    MAVEN_LOCAL {
        /**@inheritDoc */
        override fun apply(project: Project) {
            project.repositories.mavenLocal()
        }
    }
}
