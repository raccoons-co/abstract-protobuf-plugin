/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import co.raccoons.example.More;
import co.raccoons.example.Nothing;
import co.raccoons.example.UserInfo;
import com.google.protobuf.Descriptors.FileDescriptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ProtocolFile")
class ProtocolFileTest {

    @ParameterizedTest
    @DisplayName("converts file name to Pascal case")
    @ValueSource(strings = {
            "nothing_else.proto",
            "nothing_else_.proto",
            "_nothing_else.proto",
            "_nothing_else_.proto"
    })
    void convertsSnakeToPascalCase(String sample) {
        assertEquals("NothingElse", JavaProtoName.SimpleName.fromProtoName(sample));
    }

    @Test
    @DisplayName("converts file name to Pascal case")
    void convertsSnakeToPascalCase() {
        var sample = "evenTs__aS.proto";
        assertEquals("EvenTsAS", JavaProtoName.SimpleName.fromProtoName(sample));
    }

    @ParameterizedTest
    @MethodSource("testProtoFiles")
    @DisplayName("generates derived outer class name")
    void nothing(String expected, FileDescriptor fileDescriptor) {
        var protocolFile = ProtocolFile.of(fileDescriptor);
        assertEquals(expected, protocolFile.outerClass());
    }

    private static Stream<Arguments> testProtoFiles() {
        return Stream.of(
                Arguments.of("MoreProto", More.getDescriptor().getFile()),
                Arguments.of("NothingOuterClass", Nothing.getDescriptor().getFile()),
                Arguments.of("UserInfoSample", UserInfo.getDescriptor().getFile())
        );
    }
}