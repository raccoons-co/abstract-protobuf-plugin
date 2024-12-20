/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

/**
 * Implementation dependency.
 */
data class Implementation(
    private val group: String,
    private val name: String,
    private val version: String
) : Dependency(CONFIGURATION_NAME, DependencyNotation(group, name, version)) {

    companion object {
        private const val CONFIGURATION_NAME = "implementation"
    }
}
