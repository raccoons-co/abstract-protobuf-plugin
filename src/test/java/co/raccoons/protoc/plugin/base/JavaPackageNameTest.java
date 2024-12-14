/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.base;

import co.raccoons.example.Nothing;
import co.raccoons.example.NothingOuterClass;
import co.raccoons.example.TopLevelEnum;
import com.google.protobuf.Descriptors.FileDescriptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("JavaPackageName")
class JavaPackageNameTest {

    @Test
    @DisplayName("not accept `null`")
    void notAcceptNull() {
        assertThrows(NullPointerException.class, () -> JavaPackageName.of(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\t", "\n"})
    @DisplayName("not accept empty or blank")
    void notAcceptEmptyOrBlank(String value) {
        assertThrows(IllegalArgumentException.class, () -> JavaPackageName.of(value));
    }

    @ParameterizedTest
    @MethodSource("messages")
    @DisplayName("if java_package is defined")
    void hasCorrectJavaPackageName(FileDescriptor protoFile) {
        var javaPackageName = JavaPackageName.from(protoFile);
        assertEquals("co.raccoons.example", javaPackageName.value());
    }

    private static Stream<Arguments> messages() {
        return Stream.of(
                Arguments.of(Nothing.getDescriptor().getFile()),
                Arguments.of(Nothing.Else.getDescriptor().getFile()),
                Arguments.of(Nothing.Else.Matters.getDescriptor().getFile()),
                Arguments.of(TopLevelEnum.getDescriptor().getFile()),
                Arguments.of(Nothing.NothingEnum.getDescriptor().getFile()),
                Arguments.of(Nothing.Else.ElseEnum.getDescriptor().getFile()),
                Arguments.of(Nothing.Else.Matters.MattersEnum.getDescriptor().getFile()),
                Arguments.of(NothingOuterClass.getDescriptor().getFile())
        );
    }
}