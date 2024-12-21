/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.test

import co.raccoons.gradle.java.Dependency
import co.raccoons.gradle.java.DependencyScope
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestJUnitConfiguration private constructor(
    private val dependencyScope: DependencyScope
) : Plugin<Project> {

    companion object {

        /** Returns new plugin configuration builder. */
        fun newBuilder() = Builder()

        /** The configuration builder */
        class Builder {

            private val dependencyScopeBuilder = DependencyScope.newBuilder()

            fun addDependency(dependency: Dependency): Builder {
                this.dependencyScopeBuilder.add(dependency)
                return this
            }

            fun build() = TestJUnitConfiguration(this.dependencyScopeBuilder.build())
        }
    }

    /** @inheritDoc */
    override fun apply(project: Project) {
        this.setupPlugin(project)
    }

    private fun setupPlugin(project: Project) {
        dependencyScope.apply(project)

        project.tasks
            .withType(Test::class.java)
            .configureEach { test ->
                test.useJUnitPlatform()
            }
    }
}
