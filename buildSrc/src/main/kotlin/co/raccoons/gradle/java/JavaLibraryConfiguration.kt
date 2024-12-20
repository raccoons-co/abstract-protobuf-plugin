/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The Java Library plugin configuration.
 */
class JavaLibraryConfiguration private constructor(
    private val dependencyScope: DependencyScope
) : Plugin<Project> {

    companion object {

        private const val JAVA_LIBRARY_PLUGIN_ID = "java-library"

        /** Returns default configuration of the Java Library plugin. */
        fun default(): JavaLibraryConfiguration = newBuilder().build()

        /** Returns new plugin configuration builder. */
        fun newBuilder() = Builder()

        /** The configuration builder */
        class Builder {

            private val dependencyScopeBuilder = DependencyScope.newBuilder()

            fun addDependency(dependency: Dependency): Builder {
                dependencyScopeBuilder.add(dependency)
                return this
            }

            fun build() = JavaLibraryConfiguration(this.dependencyScopeBuilder.build())
        }
    }

    /** @inheritDoc */
    override fun apply(project: Project) {
        project.plugins.apply(JAVA_LIBRARY_PLUGIN_ID)
        dependencyScope.apply(project)
    }
}
