package co.raccoons.protoc.plugin.file;

import co.raccoons.protoc.plugin.InsertionPoint;
import co.raccoons.protoc.plugin.ProtobufType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("InsertionPoint")
class InsertionPointTest {

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("has correct format")
    void hasCorrectFormat(String expected, InsertionPoint insertionPoint) {
        var type = protobufType();
        assertEquals(expected, insertionPoint.forType(type));
    }

    private ProtobufType protobufType() {
        return ProtobufType.newBuilder()
                .setName("raccoons.protoc.Nothing")
                .build();
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of("message_implements:raccoons.protoc.Nothing", InsertionPoint.message_implements),
                Arguments.of("builder_implements:raccoons.protoc.Nothing", InsertionPoint.builder_implements),
                Arguments.of("interface_extends:raccoons.protoc.Nothing", InsertionPoint.interface_extends),
                Arguments.of("class_scope:raccoons.protoc.Nothing", InsertionPoint.class_scope),
                Arguments.of("builder_scope:raccoons.protoc.Nothing", InsertionPoint.builder_scope),
                Arguments.of("enum_scope:raccoons.protoc.Nothing", InsertionPoint.enum_scope),
                Arguments.of("outer_class_scope", InsertionPoint.outer_class_scope)
        );
    }
}
