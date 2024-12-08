/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.extra;

import co.raccoons.protoc.extra.given.ClassMock;
import co.raccoons.protoc.extra.given.SubClassMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Content")
class ContentTest {

    @Test
    @DisplayName("inheritance of class")
    void hasCorrectString() {
        var content = Content.inheritanceOf(ClassMock.class);
        assertEquals("co.raccoons.protoc.extra.given.ClassMock,", content);
    }

    @Test
    @DisplayName("inheritance of subclass")
    void nothing() {
        var content = Content.inheritanceOf(SubClassMock.class);
        assertEquals("co.raccoons.protoc.extra.given.SubClassMock,", content);
    }
}