/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Descriptors.FileDescriptor;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class ProtocolFile implements ProtoParser, JavaProtoName {

    private final FileDescriptor file;
    private final ProtocolTypeSet types;

    private ProtocolFile(FileDescriptor file, ProtocolTypeSet types) {
        this.file = checkNotNull(file);
        this.types = checkNotNull(types);
    }

    public static ProtocolFile of(FileDescriptor file) {
        var builder = ProtocolTypeSet.newBuilder();
        new Parser(builder).walk(file);
        var types = builder.build();
        return new ProtocolFile(file, types);
    }

    @Override
    public FileDescriptor file() {
        return file;
    }

    @Override
    public ProtocolTypeSet types() {
        return types;
    }

    public static void walk(FileDescriptor protoFile) {
        walker(protoFile).walk();
    }

    private static ProtocolFileWalker walker(FileDescriptor protoFile) {
        return protoFile.getOptions().getJavaMultipleFiles()
                ? new JavaMultipleFile(protoFile)
                : new JavaSingleFile(protoFile);
    }
}
