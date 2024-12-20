/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The scopes of the dependencies.
 */
internal class DependencyScope(
    private val dependencies: Map<String, List<String>>
) : Plugin<Project> {

    companion object {

        /** Returns new dependency scope configuration builder. */
        fun newBuilder() = Builder()

        /** The configuration builder */
        class Builder {

            private val dependencies = mutableMapOf<String, MutableList<String>>()

            /** Adds dependency to the corresponding configuration scope. */
            fun add(dependency: Dependency): Builder {
                this.dependencies
                    .getOrPut(dependency.configurationScope()) {
                        mutableListOf()
                    }
                    .add(dependency.dependencyNotation().toString())

                return this
            }

            /** Returns new dependency scope configuration. */
            fun build() = DependencyScope(this.dependencies.toMap())
        }
    }

    /** @inheritDoc */
    override fun apply(project: Project) {
        for ((configurationScope, dependencies) in dependencies) {
            for (dependency in dependencies) {
                project.dependencies.add(configurationScope, dependency)
            }
        }
    }
}
