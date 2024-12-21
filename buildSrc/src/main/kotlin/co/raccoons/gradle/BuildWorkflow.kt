/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle build configurator.
 */
object BuildWorkflow {

    /**
     * Returns build configuration handler of this project.
     */
    fun of(project: Project): BuildConfigurationHandler {
        /** inheritDoc */
        class Handler(private val project: Project) : BuildConfigurationHandler {
            /** inheritDoc */
            override fun use(plugin: Plugin<Project>): BuildConfigurationHandler {
                plugin.apply(this.project)
                return this
            }

            /** inheritDoc */
            override fun setGroup(group: String): BuildConfigurationHandler {
                this.project.group = group
                return this
            }

            /** inheritDoc */
            override fun setVersion(version: String): BuildConfigurationHandler {
                this.project.version = version
                return this
            }
        }

        return Handler(project)
    }

    /**
     * Build configuration handler that applies configuration to a project.
     */
    interface BuildConfigurationHandler {

        /**
         * Puts plugin into operation
         */
        fun use(plugin: Plugin<Project>): BuildConfigurationHandler

        /**
         * Sets the group of this project.
         *
         * @param group The group of this project.
         */
        fun setGroup(group: String): BuildConfigurationHandler

        /**
         * Sets the version of this project.
         *
         * @param version The version of this project.
         */
        fun setVersion(version: String): BuildConfigurationHandler
    }
}
