/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("InsertionPoint")
class ProtocExtraTest {

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("has correct identifier")
    void hasCorrectFormat(String expected, ProtocExtra protocExtra) {
        var type = protobufType();
        var insertionPoint = protocExtra.newInsertionPoint(type);
        assertEquals(expected, insertionPoint.getIdentifier());
    }

    private ProtobufType protobufType() {
        return ProtobufType.newBuilder()
                .setName("raccoons.protoc.Nothing")
                .build();
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of("message_implements:raccoons.protoc.Nothing", ProtocExtra.message_implements),
                Arguments.of("builder_implements:raccoons.protoc.Nothing", ProtocExtra.builder_implements),
                Arguments.of("interface_extends:raccoons.protoc.Nothing", ProtocExtra.interface_extends),
                Arguments.of("class_scope:raccoons.protoc.Nothing", ProtocExtra.class_scope),
                Arguments.of("builder_scope:raccoons.protoc.Nothing", ProtocExtra.builder_scope),
                Arguments.of("enum_scope:raccoons.protoc.Nothing", ProtocExtra.enum_scope),
                Arguments.of("outer_class_scope", ProtocExtra.outer_class_scope)
        );
    }
}
