/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.base;

import co.raccoons.protoc.plugin.ProtobufType.FileName;
import co.raccoons.protoc.plugin.ProtobufTypeSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

/**
 * Protobuf type collector that generates multiple Java files.
 *
 * @see <a href="https://protobuf.dev/reference/java/java-generated/">
 * option java_multiple_files = true;</a>
 */
final class JavaMultipleFile extends ProtobufTypeCollector {

    public JavaMultipleFile(FileDescriptor protoFile, ProtobufTypeSet.Builder builder) {
        super(protoFile, builder);
    }

    @Override
    protected FileName fileNameFor(ServiceDescriptor service) {
        return FileName.newBuilder()
                .setName(JavaFileName.of(service).forClass())
                .build();
    }

    @Override
    protected FileName fileNameFor(EnumDescriptor enumType) {
        return FileName.newBuilder()
                .setName(JavaFileName.of(enumType).forClass())
                .build();
    }

    @Override
    protected FileName fileNameFor(Descriptor messageType) {
        return FileName.newBuilder()
                .setName(JavaFileName.of(messageType).forClass())
                .setMessageOrBuilderName(JavaFileName.of(messageType).forMessageOrBuilder())
                .setOuterClassName(JavaFileName.of(messageType).forOuterClass())
                .build();
    }

    protected FileName fileNameForInner(Descriptor messageType) {
        return FileName.newBuilder()
                .setName(JavaFileName.of(messageType).forClass())
                .setMessageOrBuilderName(JavaFileName.of(messageType).forClass())
                .setOuterClassName(JavaFileName.of(messageType).forOuterClass())
                .build();
    }

}
