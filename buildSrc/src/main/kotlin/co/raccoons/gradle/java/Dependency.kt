/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

abstract class Dependency(
    private val configurationName: String,
    private val dependencyNotation: DependencyNotation
) {

    /** Returns dependency configuration scope name. */
    fun configurationScope() = configurationName

    /** Returns dependency notation. */
    fun dependencyNotation() = dependencyNotation
}
