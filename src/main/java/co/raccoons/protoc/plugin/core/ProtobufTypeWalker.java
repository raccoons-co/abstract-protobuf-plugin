package co.raccoons.protoc.plugin.core;

import co.raccoons.protoc.plugin.ProtobufType;
import co.raccoons.protoc.plugin.ProtobufType.FileName;
import co.raccoons.protoc.plugin.ProtocolType;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract protocol message types tree walker.
 * <p>
 * The collector walks through the tree of types in the .proto file and adds
 * them to type set builder.
 */
abstract class ProtobufTypeWalker {

    private final FileDescriptor protoFile;

    protected ProtobufTypeWalker(FileDescriptor protoFile) {
        this.protoFile = checkNotNull(protoFile);
    }

    /**
     * Process type collection from .proto file.
     */
    public final void walk() {
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
        var type = ProtobufType.newBuilder()
                .setName(service.getFullName())
                .setJavaFileName(javaFileName)
                .setService(service.toProto())
                .build();

        ProtocolType.newBuilder()
                .setProtobufType(type)
                .setService(service.toProto())
                .build()
                .post();

    }

    private void addNewEnumType(EnumDescriptor enumType, FileName javaFileName) {
        var type = ProtobufType.newBuilder()
                .setName(enumType.getFullName())
                .setJavaFileName(javaFileName)
                .setEnumType(enumType.toProto())
                .build();

        ProtocolType.newBuilder()
                .setProtobufType(type)
                .setEnumType(enumType.toProto())
                .build()
                .post();
    }

    private void addNewMessageType(Descriptor messageType, FileName javaFileName) {
        var type = ProtobufType.newBuilder()
                .setName(messageType.getFullName())
                .setJavaFileName(javaFileName)
                .setMessageType(messageType.toProto())
                .build();

        ProtocolType.newBuilder()
                .setProtobufType(type)
                .setMessageType(messageType.toProto())
                .build()
                .post();
    }
}
