/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import com.google.common.annotations.VisibleForTesting;
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
public final class ProtocolTypeSet {

    private final ImmutableSet<ServiceDescriptor> services;
    private final ImmutableSet<EnumDescriptor> enumTypes;
    private final ImmutableSet<Descriptor> messageTypes;

    private ProtocolTypeSet(Builder builder) {
        this.services = ImmutableSet.copyOf(builder.services);
        this.enumTypes = ImmutableSet.copyOf(builder.enumTypes);
        this.messageTypes = ImmutableSet.copyOf(builder.messageTypes);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private final Set<ServiceDescriptor> services = new HashSet<>();
        private final Set<EnumDescriptor> enumTypes = new HashSet<>();
        private final Set<Descriptor> messageTypes = new HashSet<>();

        private Builder() {
        }

        public Builder add(ServiceDescriptor service){
            checkNotNull(service);
            services.add(service);
            return this;
        }

        public Builder add(EnumDescriptor enumType){
            checkNotNull(enumType);
            enumTypes.add(enumType);
            return this;
        }

        public Builder add(Descriptor messageType){
            checkNotNull(messageType);
            messageTypes.add(messageType);
            return this;
        }

        public ProtocolTypeSet build() {
            return new ProtocolTypeSet(this);
        }
    }

    public ImmutableSet<ServiceDescriptor> services() {
        return services;
    }

    public ImmutableSet<EnumDescriptor> enumTypes() {
        return enumTypes;
    }

    public ImmutableSet<Descriptor> messageTypes() {
        return messageTypes;
    }

    public boolean contains(String typeName){
        return allTypeNames().contains(typeName);
    }

    @VisibleForTesting
    ImmutableSet<GenericDescriptor> allTypes() {
        return ImmutableSet.<GenericDescriptor>builder()
                .addAll(services)
                .addAll(enumTypes)
                .addAll(messageTypes)
                .build();
    }

    private ImmutableSet<String> allTypeNames() {
        return allTypes()
                .stream()
                .map(GenericDescriptor::getName)
                .collect(toImmutableSet());
    }
}
