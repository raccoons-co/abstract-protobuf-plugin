/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FileDescriptor;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

interface ProtoParser {

    final class Parser {

        private final ProtocolTypeSet.Builder builder;

        Parser(ProtocolTypeSet.Builder builder) {
            this.builder = checkNotNull(builder);
        }

        void walk(FileDescriptor protoFile) {
            walkTopLevelServices(protoFile.getServices());
            walkTopLevelEnumTypes(protoFile.getEnumTypes());
            walkTopLevelMessageTypes(protoFile.getMessageTypes());
        }

        @SuppressWarnings("CheckReturnValue")
        private void walkTopLevelServices(List<Descriptors.ServiceDescriptor> serviceList) {
            for (var service : serviceList) {
                builder.add(service);
            }
        }

        @SuppressWarnings("CheckReturnValue")
        private void walkTopLevelEnumTypes(List<Descriptors.EnumDescriptor> enumTypeList) {
            for (var enumType : enumTypeList) {
                builder.add(enumType);
            }
        }

        @SuppressWarnings("CheckReturnValue")
        private void walkTopLevelMessageTypes(List<Descriptors.Descriptor> messageTypeList) {
            for (var messageType : messageTypeList) {
                builder.add(messageType);
                flatten(messageType);
            }
        }

        private void flatten(Descriptors.Descriptor messageType) {
            walkInnerEnumTypes(messageType.getEnumTypes());
            walkNestedTypes(messageType.getNestedTypes());
        }

        @SuppressWarnings("CheckReturnValue")
        private void walkNestedTypes(List<Descriptors.Descriptor> messageTypeList) {
            for (var messageType : messageTypeList) {
                builder.add(messageType);
                flatten(messageType);
            }
        }

        @SuppressWarnings("CheckReturnValue")
        private void walkInnerEnumTypes(List<Descriptors.EnumDescriptor> enumTypeList) {
            for (var enumType : enumTypeList) {
                builder.add(enumType);
            }
        }
    }
}
