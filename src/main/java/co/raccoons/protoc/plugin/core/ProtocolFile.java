/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Descriptors.FileDescriptor;

/**
 * Utility class.
 */
@Immutable
public final class ProtocolFile {

    private ProtocolFile() {
    }

    public static void walk(FileDescriptor protoFile) {
        walker(protoFile).walk();
    }

    private static ProtocolTypeWalker walker(FileDescriptor protoFile) {
        return protoFile.getOptions().getJavaMultipleFiles()
                ? new JavaMultipleFile(protoFile)
                : new JavaSingleFile(protoFile);
    }
}
