/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.publish

import co.raccoons.gradle.publish.maven.Publication
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

/**
 * Maven publish plugin configuration.
 */
class MavenPublishConfiguration(private val publication: Publication) : Plugin<Project> {

    companion object {
        private const val MAVEN_PUBLISH_PLUGIN_ID = "maven-publish"
    }

    /** @inheritDoc */
    override fun apply(project: Project) {
        this.setupPlugin(project)
        this.addTaskPackageJavaSources(project)
        this.addTaskPackageJavadoc(project)
        this.createMavenPublication(project)
    }

    private fun setupPlugin(project: Project) {
        project.plugins.apply(MAVEN_PUBLISH_PLUGIN_ID)
    }

    private fun addTaskPackageJavadoc(project: Project) {
        project.extensions
            .getByType(JavaPluginExtension::class.java)
            .withJavadocJar()
    }

    private fun addTaskPackageJavaSources(project: Project) {
        project.extensions
            .getByType(JavaPluginExtension::class.java)
            .withSourcesJar()
    }

    private fun createMavenPublication(project: Project) {
        project.extensions
            .getByType(PublishingExtension::class.java)
            .publications { container ->
                container.create("mavenJava", MavenPublication::class.java) { publication ->
                    publication.artifactId = this.publication.artifactId

                    publication.from(project.components.getByName("java"))

                    publication.pom { pom ->
                        pom.name.set(this.publication.pom.name)
                        pom.description.set(this.publication.pom.description)
                        pom.url.set(this.publication.pom.url)

                        pom.licenses { spec ->
                            spec.license { pomLicense ->
                                pomLicense.name.set(this.publication.pom.license.name)
                                pomLicense.url.set(this.publication.pom.license.url)
                            }
                        }
                    }
                }
            }
    }
}
