/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

/**
 * Test implementation dependency.
 */
data class TestImplementation(
    private val group: String,
    private val name: String,
    private val version: String
) : Dependency(CONFIGURATION_NAME, DependencyNotation(group, name, version)) {

    companion object {
        private const val CONFIGURATION_NAME = "testImplementation"
    }
}
