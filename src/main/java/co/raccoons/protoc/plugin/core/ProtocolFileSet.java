/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A set of Proto files of with their dependencies.
 */
@Immutable
public final class ProtocolFileSet {

    private final ImmutableMap<String, FileDescriptor> files;

    private ProtocolFileSet(ImmutableMap<String, FileDescriptor> files) {
        this.files = checkNotNull(files);
    }

    /**
     * Returns a new instance of {@code ProtobufFileSet} for the given proto
     * file list.
     */
    public static ProtocolFileSet of(Iterable<FileDescriptorProto> protos) {
        checkNotNull(protos);
        var files = files(protos);
        return new ProtocolFileSet(files);
    }

    /**
     * Obtains proto file descripto for the given file name.
     */
    public FileDescriptor file(String fileName){
        checkNotNull(fileName);
        return files.get(fileName);
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
}
