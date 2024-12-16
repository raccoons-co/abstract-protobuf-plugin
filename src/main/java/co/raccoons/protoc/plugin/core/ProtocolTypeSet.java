/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import co.raccoons.protoc.plugin.ProtocolType;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

@Immutable
final class ProtocolTypeSet {

    private final ImmutableSet<ServiceDescriptor> rawServices;
    private final ImmutableSet<EnumDescriptor> rawEnumTypes;
    private final ImmutableSet<Descriptor> rawMessageTypes;

    private ProtocolTypeSet(Builder builder) {
        this.rawServices = ImmutableSet.copyOf(builder.rawServices);
        this.rawEnumTypes = ImmutableSet.copyOf(builder.rawEnumTypes);
        this.rawMessageTypes = ImmutableSet.copyOf(builder.rawMessageTypes);
    }

    public boolean contains(String typeName) {
        return allRawTypeNames().contains(typeName);
    }

    public ImmutableSet<ProtocolType> values(JavaProtoName javaProtoName) {
        var protocolTypeMapper = new ProtocolTypeMapper(javaProtoName);
        return ImmutableSet.<ProtocolType>builder()
                .addAll(services(protocolTypeMapper))
                .addAll(enumTypes(protocolTypeMapper))
                .addAll(messageTypes(protocolTypeMapper))
                .build();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private final Set<ServiceDescriptor> rawServices = new HashSet<>();
        private final Set<EnumDescriptor> rawEnumTypes = new HashSet<>();
        private final Set<Descriptor> rawMessageTypes = new HashSet<>();

        private Builder() {
        }

        public Builder add(ServiceDescriptor service) {
            checkNotNull(service);
            rawServices.add(service);
            return this;
        }

        public Builder add(EnumDescriptor enumType) {
            checkNotNull(enumType);
            rawEnumTypes.add(enumType);
            return this;
        }

        public Builder add(Descriptor messageType) {
            checkNotNull(messageType);
            rawMessageTypes.add(messageType);
            return this;
        }

        public ProtocolTypeSet build() {
            return new ProtocolTypeSet(this);
        }
    }

    private ImmutableSet<String> allRawTypeNames() {
        return allRawTypes()
                .stream()
                .map(GenericDescriptor::getName)
                .collect(toImmutableSet());
    }

    private ImmutableSet<GenericDescriptor> allRawTypes() {
        return ImmutableSet.<GenericDescriptor>builder()
                .addAll(rawServices)
                .addAll(rawEnumTypes)
                .addAll(rawMessageTypes)
                .build();
    }

    private ImmutableSet<ProtocolType> services(ProtocolTypeMapper mapper) {
        return rawServices.stream()
                .map(mapper::service)
                .collect(toImmutableSet());
    }

    private ImmutableSet<ProtocolType> enumTypes(ProtocolTypeMapper mapper) {
        return rawEnumTypes.stream()
                .map(mapper::enumType)
                .collect(toImmutableSet());
    }

    private ImmutableSet<ProtocolType> messageTypes(ProtocolTypeMapper mapper) {
        return rawMessageTypes.stream()
                .map(mapper::messageType)
                .collect(toImmutableSet());
    }

    private static final class ProtocolTypeMapper {

        private final JavaProtoName javaProtoName;

        public ProtocolTypeMapper(JavaProtoName javaProtoName) {
            this.javaProtoName = checkNotNull(javaProtoName);
        }

        private ProtocolType service(ServiceDescriptor service) {
            return ProtocolType.newBuilder()
                    .setName(service.getFullName())
                    .setService(service.toProto())
                    .build();
        }

        private ProtocolType enumType(EnumDescriptor enumType) {
            return ProtocolType.newBuilder()
                    .setName(enumType.getFullName())
                    .setEnumType(enumType.toProto())
                    .build();
        }

        private ProtocolType messageType(Descriptor messageType) {
            var fileName = ProtocolType.FileName.newBuilder()
                    .setName(javaProtoName.messageFileName(messageType))
                    .setMessageOrBuilderName(javaProtoName.orBuilderFileName(messageType))
                    .setOuterClassName(javaProtoName.outerClassFileName())
                    .build();

            return ProtocolType.newBuilder()
                    .setName(messageType.getFullName())
                    .setMessageType(messageType.toProto())
                    .setJavaFileName(fileName)
                    .build();
        }
    }
}
