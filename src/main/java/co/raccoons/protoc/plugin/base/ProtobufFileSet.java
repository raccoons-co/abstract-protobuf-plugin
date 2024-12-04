/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.base;

import co.raccoons.protoc.plugin.ProtobufTypeSet;
import co.raccoons.protoc.plugin.ProtobufTypeSet.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.ProtocolStringList;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A set of Proto files of with their dependencies.
 */
@Immutable
public final class ProtobufFileSet {

    private final ImmutableMap<String, FileDescriptor> files;

    private ProtobufFileSet(ImmutableMap<String, FileDescriptor> files) {
        this.files = checkNotNull(files);
    }

    /**
     * Returns a new instance of {@code ProtobufFileSet} for the given proto
     * file list.
     */
    public static ProtobufFileSet of(Iterable<FileDescriptorProto> protos) {
        checkNotNull(protos);
        var files = files(protos);
        return new ProtobufFileSet(files);
    }

    /**
     * Obtains a new instance of {@code ProtobufTypeSet} from the list of
     * requested to generate files.
     */
    public ProtobufTypeSet newProtobufTypeSet(ProtocolStringList fileToGenerateList) {
        checkNotNull(fileToGenerateList);
        var builder = ProtobufTypeSet.newBuilder();
        for (var fileName : fileToGenerateList) {
            newCollector(fileName, builder).collect();
        }
        return builder.build();
    }

    private static ImmutableMap<String, FileDescriptor> files(Iterable<FileDescriptorProto> protos) {
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

    private ProtobufTypeCollector newCollector(String fileName, Builder builder) {
        var protoFile = files.get(fileName);
        checkNotNull(protoFile);
        return protoFile.getOptions().getJavaMultipleFiles()
                ? new JavaMultipleFile(protoFile, builder)
                : new JavaSingleFile(protoFile, builder);
    }
}
