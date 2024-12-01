package co.raccoons.protoc.plugin.protofile;

import co.raccoons.protoc.plugin.ProtobufType;
import co.raccoons.protoc.plugin.ProtobufTypeFileName;
import co.raccoons.protoc.plugin.ProtobufTypeSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ProtobufTypeCollector {

    private final FileDescriptor protoFile;
    private final ProtobufTypeSet.Builder builder;

    protected ProtobufTypeCollector(FileDescriptor protoFile,
                                    ProtobufTypeSet.Builder builder) {
        this.protoFile = checkNotNull(protoFile);
        this.builder = checkNotNull(builder);
    }

    public final void collect() {
        checkNotNull(protoFile);
        walkTopLevelServices(protoFile.getServices());
        walkTopLevelEnumTypes(protoFile.getEnumTypes());
        walkTopLevelMessageTypes(protoFile.getMessageTypes());
    }

    protected abstract ProtobufTypeFileName fileNameFor(ServiceDescriptor service);

    protected abstract ProtobufTypeFileName fileNameFor(EnumDescriptor enumType);

    protected abstract ProtobufTypeFileName fileNameFor(Descriptor messageType);

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
            flatten(messageType, fileNameFor(messageType));
        }
    }

    private void flatten(Descriptor messageType,
                         ProtobufTypeFileName javaFileName) {
        addNewMessageType(messageType, javaFileName);
        walkInnerEnumTypes(messageType.getEnumTypes(), javaFileName);
        walkNestedTypes(messageType.getNestedTypes(), javaFileName);
    }

    private void walkNestedTypes(List<Descriptor> messageTypeList,
                                 ProtobufTypeFileName javaFileName) {
        for (var messageType : messageTypeList) {
            flatten(messageType, javaFileName);
        }
    }

    private void walkInnerEnumTypes(List<EnumDescriptor> enumTypeList,
                                    ProtobufTypeFileName javaFileName) {
        for (var enumType : enumTypeList) {
            addNewEnumType(enumType, javaFileName);
        }
    }

    private void addNewService(ServiceDescriptor service,
                               ProtobufTypeFileName javaFileName) {
        builder.addService(
                ProtobufType.newBuilder()
                        .setName(service.getFullName())
                        .setJavaFileName(javaFileName)
                        .setService(service.toProto())
                        .build()
        );
    }

    private void addNewEnumType(EnumDescriptor enumType,
                                ProtobufTypeFileName javaFileName) {
        builder.addEnumType(
                ProtobufType.newBuilder()
                        .setName(enumType.getFullName())
                        .setJavaFileName(javaFileName)
                        .setEnumType(enumType.toProto())
                        .build()
        );
    }

    private void addNewMessageType(Descriptor messageType,
                                   ProtobufTypeFileName javaFileName) {
        builder.addMessageType(
                ProtobufType.newBuilder()
                        .setName(messageType.getFullName())
                        .setJavaFileName(javaFileName)
                        .setMessageType(messageType.toProto())
                        .build()
        );
    }
}
