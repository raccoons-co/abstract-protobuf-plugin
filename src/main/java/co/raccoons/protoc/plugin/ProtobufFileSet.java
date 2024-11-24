/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;
import com.google.protobuf.ProtocolStringList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

@Immutable
public final class ProtobufFileSet {

    private final ImmutableMap<String, FileDescriptor> files;

    private ProtobufFileSet(ImmutableMap<String, FileDescriptor> files) {
        this.files = files;
    }

    public static ProtobufFileSet of(Iterable<FileDescriptorProto> protos) {
        return walk(protos);
    }

    public ProtobufTypeSet newProtobufTypeSet(ProtocolStringList fileToGenerateList) {
        return new TypeCollector().from(fileToGenerateList);
    }

    private static ProtobufFileSet walk(Iterable<FileDescriptorProto> protos) {
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
        var result = ImmutableMap.copyOf(files);
        return new ProtobufFileSet(result);
    }

    private final class TypeCollector {

        private final Set<ProtobufType> messageTypes = new HashSet<>();
        private final Set<ProtobufType> enumTypes = new HashSet<>();
        private final Set<ProtobufType> services = new HashSet<>();

        public ProtobufTypeSet from(ProtocolStringList fileToGenerateList) {
            for (var fileName : fileToGenerateList) {
                var protoFile = files.get(fileName);
                checkNotNull(protoFile);
                walkServices(protoFile);
                walkEnumTypes(protoFile);
                walkMessageTypes(protoFile);
            }
            return ProtobufTypeSet.newBuilder()
                    .addAllMessageType(messageTypes)
                    .addAllEnumType(enumTypes)
                    .addAllService(services)
                    .build();
        }

        private void walkServices(FileDescriptor protoFile) {
            for (var service : protoFile.getServices()) {
                services.add(
                        ProtobufType.newBuilder()
                                .setName(service.getFullName())
                                .setJavaFileName(javaFileName(service))
                                .setService(service.toProto())
                                .build()
                );
            }
        }

        private void walkEnumTypes(FileDescriptor protoFile) {
            for (var enumType : protoFile.getEnumTypes()) {
                enumTypes.add(
                        ProtobufType.newBuilder()
                                .setName(enumType.getFullName())
                                .setJavaFileName(javaFileName(enumType))
                                .setEnumType(enumType.toProto())
                                .build()
                );
            }
        }

        private void walkMessageTypes(FileDescriptor protoFile) {
            for (var messageType : protoFile.getMessageTypes()) {
                flatten(messageType, javaFileName(messageType));
            }
        }

        private void flatten(Descriptor messageType, String javaFileName) {
            messageTypes.add(
                    ProtobufType.newBuilder()
                            .setName(messageType.getFullName())
                            .setJavaFileName(javaFileName)
                            .setMessageType(messageType.toProto())
                            .build()
            );

            for (var nestedType : messageType.getNestedTypes()) {
                flatten(nestedType, javaFileName);
            }

            walkNestedEnumTypes(messageType, javaFileName);
        }

        private void walkNestedEnumTypes(Descriptor messageType, String javaFileName) {
            for (var enumType : messageType.getEnumTypes()) {
                enumTypes.add(
                        ProtobufType.newBuilder()
                                .setName(enumType.getFullName())
                                .setJavaFileName(javaFileName)
                                .setEnumType(enumType.toProto())
                                .build()
                );
            }
        }

        private String javaFileName(GenericDescriptor descriptor) {
            var javaDirectory = relativeDirectory(descriptor);
            var javaSimpleName = descriptor.getName();
            return format("%s/%s.java", javaDirectory, javaSimpleName);
        }

        private String relativeDirectory(GenericDescriptor descriptor) {
            return descriptor.getFile()
                    .getOptions()
                    .getJavaPackage()
                    .replaceAll("\\.", "/");
        }
    }
}
