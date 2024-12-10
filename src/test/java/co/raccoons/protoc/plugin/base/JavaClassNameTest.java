/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.base;

import co.raccoons.example.Nothing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JavaClassName")
class JavaClassNameTest {

    @Test
    @DisplayName("of top-level message protocol type")
    void topLevelClassName() {
        var descriptor = Nothing.getDescriptor();
        var javaClassName = JavaClassName.from(descriptor);
        assertEquals("co.raccoons.example.Nothing", javaClassName.value());
    }

    @Test
    @DisplayName("of 1st level of nesting")
    void firstLevelOfNesting() {
        var descriptor = Nothing.Else.getDescriptor();
        var javaClassName = JavaClassName.from(descriptor);
        assertEquals("co.raccoons.example.Nothing$Else", javaClassName.value());
    }

    @Test
    @DisplayName("of 2nd level of nesting")
    void secondLevelOfNesting() {
        var descriptor = Nothing.Else.Matters.getDescriptor();
        var javaClassName = JavaClassName.from(descriptor);
        assertEquals("co.raccoons.example.Nothing$Else$Matters", javaClassName.value());
    }

}