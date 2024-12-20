/*
 * Copyright 2023, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.java

data class DependencyNotation(
    private val group: String,
    private val name: String,
    private val version: String
) {

    /** Returns string representation of the dependency notation. */
    override fun toString() = "$group:$name:$version"
}
