/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.protofile;

import co.raccoons.protoc.plugin.ProtobufTypeFileName;
import co.raccoons.protoc.plugin.ProtobufTypeSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

public final class JavaMultipleFile extends ProtobufTypeCollector {

    public JavaMultipleFile(FileDescriptor protoFile,
                            ProtobufTypeSet.Builder builder) {
        super(protoFile, builder);
    }

    @Override
    protected ProtobufTypeFileName fileNameFor(ServiceDescriptor service) {
        return ProtobufTypeFileName.newBuilder()
                .setName(JavaFileName.of(service).forClass())
                .build();
    }

    @Override
    protected ProtobufTypeFileName fileNameFor(EnumDescriptor enumType) {
        return ProtobufTypeFileName.newBuilder()
                .setName(JavaFileName.of(enumType).forClass())
                .build();
    }

    @Override
    protected ProtobufTypeFileName fileNameFor(Descriptor messageType) {
        return ProtobufTypeFileName.newBuilder()
                .setName(JavaFileName.of(messageType).forClass())
                .setBuilderName(JavaFileName.of(messageType).forBuilder())
                .setOuterClassName(JavaFileName.of(messageType).forOuterClass())
                .build();
    }
}

