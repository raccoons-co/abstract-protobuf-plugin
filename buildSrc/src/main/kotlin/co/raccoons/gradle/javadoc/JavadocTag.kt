/*
 * Copyright 2023, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.javadoc

class JavadocTag(private val name: String, private val head: String) {

    /** Returns string representation of the Javadoc tag.  */
    override fun toString() = "$name:a:$head:"
}
