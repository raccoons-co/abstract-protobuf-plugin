/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import co.raccoons.protoc.plugin.protofile.JavaOptionPreset;
import co.raccoons.protoc.plugin.protofile.ProtobufTypeCollector;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.ProtocolStringList;

import java.util.HashMap;
import java.util.Map;

@Immutable
public final class ProtobufFileSet {

    private final ImmutableMap<String, FileDescriptor> files;
    @SuppressWarnings("Immutable")
    private final ProtobufTypeCollector collector = new ProtobufTypeCollector();

    private ProtobufFileSet(ImmutableMap<String, FileDescriptor> files) {
        this.files = files;
    }

    public static ProtobufFileSet of(Iterable<FileDescriptorProto> protos) {
        var files = walk(protos);
        return new ProtobufFileSet(files);
    }

    public ProtobufTypeSet newProtobufTypeSet(ProtocolStringList fileToGenerateList) {
        for (var fileName : fileToGenerateList) {
            var protoFile = files.get(fileName);
            JavaOptionPreset.of(protoFile)
                    .multipleFilesOrSingle()
                    .accept(collector, protoFile);
        }
        return ProtobufTypeSet.newBuilder()
                .addAllService(collector.services())
                .addAllEnumType(collector.enumTypes())
                .addAllMessageType(collector.messageTypes())
                .build();
    }

    private static ImmutableMap<String, FileDescriptor> walk(Iterable<FileDescriptorProto> protos) {
        Map<String, FileDescriptor> files = new HashMap<>();
        for (var fileDescriptorProto : protos) {
            var dependencies =
                    fileDescriptorProto.getDependencyList()
                            .stream()
                            .map(files::get)
                            .toArray(FileDescriptor[]::new);

            try {
                var fd = FileDescriptor.buildFrom(fileDescriptorProto, dependencies);
                files.put(fileDescriptorProto.getName(), fd);
            } catch (DescriptorValidationException e) {
                throw new IllegalStateException("DescriptorProto is not valid.", e);
            }
        }
        return ImmutableMap.copyOf(files);
    }
}
