/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.base;

import co.raccoons.example.Nothing;
import co.raccoons.protoc.plugin.Identifier;
import co.raccoons.protoc.plugin.ProtocolType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdentifierTest {


    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("has correct identifier")
    void hasCorrectIdentifier(String expected, Identifier identifier) {
        var type = ProtocolType.newBuilder()
                .setName(Nothing.getDescriptor().getFullName())
                .build();

        assertEquals(expected, identifier.forType(type));
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of("message_implements:raccoons.protoc.Nothing", Identifier.message_implements),
                Arguments.of("builder_implements:raccoons.protoc.Nothing", Identifier.builder_implements),
                Arguments.of("interface_extends:raccoons.protoc.Nothing", Identifier.interface_extends),
                Arguments.of("class_scope:raccoons.protoc.Nothing", Identifier.class_scope),
                Arguments.of("builder_scope:raccoons.protoc.Nothing", Identifier.builder_scope),
                Arguments.of("enum_scope:raccoons.protoc.Nothing", Identifier.enum_scope),
                Arguments.of("outer_class_scope", Identifier.outer_class_scope)
        );
    }

}