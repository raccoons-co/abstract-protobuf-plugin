/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.protofile;

import co.raccoons.protoc.plugin.ProtobufTypeSet;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.ProtocolStringList;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class ProtobufFileSet {

    private final ImmutableMap<String, FileDescriptor> files;

    private ProtobufFileSet(ImmutableMap<String, FileDescriptor> files) {
        this.files = checkNotNull(files);
    }

    public static ProtobufFileSet of(Iterable<FileDescriptorProto> protos) {
        checkNotNull(protos);
        var files = files(protos);
        return new ProtobufFileSet(files);
    }

    public ProtobufTypeSet newProtobufTypeSet(ProtocolStringList fileToGenerateList) {
        var builder = ProtobufTypeSet.newBuilder();
        for (var fileName : fileToGenerateList) {
            var protoFile = files.get(fileName);
            JavaMultipleFilesOrSingle.of(protoFile)
                    .newProtobufTypeCollector(builder)
                    .collect();
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

    @Immutable
    private static final class JavaMultipleFilesOrSingle {

        private final FileDescriptor protoFile;

        public JavaMultipleFilesOrSingle(FileDescriptor protoFile) {
            this.protoFile = checkNotNull(protoFile);
        }

        public static JavaMultipleFilesOrSingle of(FileDescriptor protoFile) {
            checkNotNull(protoFile);
            return new JavaMultipleFilesOrSingle(protoFile);
        }

        public ProtobufTypeCollector newProtobufTypeCollector(ProtobufTypeSet.Builder builder) {
            return protoFile.getOptions().getJavaMultipleFiles()
                    ? new JavaMultipleFile(protoFile, builder)
                    : new JavaSingleFile(protoFile, builder);
        }
    }
}
