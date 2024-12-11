/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JavaFileName")
class JavaFileNameTest {

    @ParameterizedTest
    @DisplayName("converts snake to camel")
    @ValueSource(strings = {
            "nothing_else.proto",
            "nothing_else_.proto",
            "_nothing_else.proto",
            "_nothing_else_.proto"
    })
    void nothing(String sample) {
        assertEquals("NothingElse", JavaFileName.fromProtoFileName(sample));
    }
}