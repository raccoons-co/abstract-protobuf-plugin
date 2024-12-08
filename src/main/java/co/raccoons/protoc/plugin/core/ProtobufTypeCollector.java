package co.raccoons.protoc.plugin.core;

import co.raccoons.protoc.plugin.ProtobufType;
import co.raccoons.protoc.plugin.ProtobufType.FileName;
import co.raccoons.protoc.plugin.ProtobufTypeSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract Protobuf types collector.
 *
 * The collector walks through the tree of types in the .proto file and adds
 * them to type set builder.
 */
abstract class ProtobufTypeCollector {

    private final FileDescriptor protoFile;
    private final ProtobufTypeSet.Builder typeSetBuilder;

    protected ProtobufTypeCollector(FileDescriptor protoFile,
                                    ProtobufTypeSet.Builder typeSetBuilder) {
        this.protoFile = checkNotNull(protoFile);
        this.typeSetBuilder = checkNotNull(typeSetBuilder);
    }

    /**
     * Process type collection from .proto file.
     */
    public final void collect() {
        walkTopLevelServices(protoFile.getServices());
        walkTopLevelEnumTypes(protoFile.getEnumTypes());
        walkTopLevelMessageTypes(protoFile.getMessageTypes());
    }

    protected abstract FileName fileNameFor(ServiceDescriptor service);

    protected abstract FileName fileNameFor(EnumDescriptor enumType);

    protected abstract FileName fileNameFor(Descriptor messageType);

    protected abstract FileName fileNameForInner(Descriptor messageType);

    private void walkTopLevelServices(List<ServiceDescriptor> serviceList) {
        for (var service : serviceList) {
            addNewService(service, fileNameFor(service));
        }
    }

    private void walkTopLevelEnumTypes(List<EnumDescriptor> enumTypeList) {
        for (var enumType : enumTypeList) {
            addNewEnumType(enumType, fileNameFor(enumType));
        }
    }

    private void walkTopLevelMessageTypes(List<Descriptor> messageTypeList) {
        for (var messageType : messageTypeList) {
            addNewMessageType(messageType, fileNameFor(messageType));
            flatten(messageType, fileNameForInner(messageType));
        }
    }

    private void flatten(Descriptor messageType, FileName javaFileName) {
        walkInnerEnumTypes(messageType.getEnumTypes(), javaFileName);
        walkNestedTypes(messageType.getNestedTypes(), javaFileName);
    }

    private void walkNestedTypes(List<Descriptor> messageTypeList, FileName javaFileName) {
        for (var messageType : messageTypeList) {
            addNewMessageType(messageType, javaFileName);
            flatten(messageType, javaFileName);
        }
    }

    private void walkInnerEnumTypes(List<EnumDescriptor> enumTypeList, FileName javaFileName) {
        for (var enumType : enumTypeList) {
            addNewEnumType(enumType, javaFileName);
        }
    }

    private void addNewService(ServiceDescriptor service, FileName javaFileName) {
        typeSetBuilder.addService(
                ProtobufType.newBuilder()
                        .setName(service.getFullName())
                        .setJavaFileName(javaFileName)
                        .setService(service.toProto())
                        .build()
        );
    }

    private void addNewEnumType(EnumDescriptor enumType, FileName javaFileName) {
        typeSetBuilder.addEnumType(
                ProtobufType.newBuilder()
                        .setName(enumType.getFullName())
                        .setJavaFileName(javaFileName)
                        .setEnumType(enumType.toProto())
                        .build()
        );
    }

    private void addNewMessageType(Descriptor messageType, FileName javaFileName) {
        typeSetBuilder.addMessageType(
                ProtobufType.newBuilder()
                        .setName(messageType.getFullName())
                        .setJavaFileName(javaFileName)
                        .setMessageType(messageType.toProto())
                        .build()
        );
    }
}
