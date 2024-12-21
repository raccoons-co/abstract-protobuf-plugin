/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.javadoc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions

/**
 * Javadoc plugin configuration.
 */
class JavadocConfiguration private constructor(private val tags: List<String>) : Plugin<Project> {

    companion object {

        /** Returns new plugin configuration builder. */
        fun newBuilder() = Builder()

        /** The configuration builder */
        class Builder {

            private val tags = mutableListOf<String>()

            /** Adds Javadoc tag list. */
            fun addTag(tag: JavadocTag): Builder {
                tags.add(tag.toString())
                return this
            }

            /** Returns new Javadoc plugin configuration. */
            fun build() = JavadocConfiguration(tags.toList())
        }
    }

    /** @inheritDoc */
    override fun apply(project: Project) {
        project.tasks
            .withType(Javadoc::class.java)
            .configureEach { javadoc ->
                (javadoc.options as StandardJavadocDocletOptions).tags(tags)
            }
    }
}
