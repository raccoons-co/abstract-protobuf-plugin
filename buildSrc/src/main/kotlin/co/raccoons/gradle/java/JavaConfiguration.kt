/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

/**
 * The Java plugin configuration.
 */
class JavaConfiguration(private val manifest: Manifest) : Plugin<Project> {

    companion object {
        private const val JAVA_PLUGIN_ID = "java"
    }

    /** @inheritDoc */
    override fun apply(project: Project) {
        this.setupPlugin(project)
        this.configureTaskJar(project)
    }

    private fun setupPlugin(project: Project) {
        project.plugins.apply(JAVA_PLUGIN_ID)
    }

    private fun configureTaskJar(project: Project) {
        project.tasks
            .withType(Jar::class.java)
            .configureEach { jar ->
                jar.manifest.attributes
                    .putAll(this.manifest.attributesMap)
            }
    }
}
