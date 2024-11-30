package co.raccoons.protoc.plugin.protofile;

import co.raccoons.protoc.plugin.ProtobufType;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;


public final class ProtobufTypeCollector implements ProtoFileVisitor {

    private final Set<ProtobufType> messageTypes = new HashSet<>();
    private final Set<ProtobufType> enumTypes = new HashSet<>();
    private final Set<ProtobufType> services = new HashSet<>();

    @Override
    public void visit(JavaMultipleFile visitor, FileDescriptor protoFile) {
        walkTopLevelServices(protoFile.getServices());
        walkTopLevelEnumTypes(protoFile.getEnumTypes());
        walkTopLevelMessageTypes(protoFile.getMessageTypes());
    }

    @Override
    public void visit(JavaSingleFile visitor, FileDescriptor protoFile) {
        var javaFileName = javaFileName(protoFile);
        walkTopLevelServices(protoFile.getServices(), javaFileName);
        walkTopLevelEnumTypes(protoFile.getEnumTypes(), javaFileName);
        walkTopLevelMessageTypes(protoFile.getMessageTypes(), javaFileName);
    }

    public ImmutableSet<ProtobufType> messageTypes() {
        return ImmutableSet.copyOf(messageTypes);
    }

    public ImmutableSet<ProtobufType> enumTypes() {
        return ImmutableSet.copyOf(enumTypes);
    }

    public ImmutableSet<ProtobufType> services() {
        return ImmutableSet.copyOf(services);
    }

    private void walkTopLevelServices(List<ServiceDescriptor> serviceList) {
        for (var service : serviceList) {
            addNewService(service, javaFileName(service));
        }
    }

    private void walkTopLevelServices(List<ServiceDescriptor> serviceList, String javaFileName) {
        for (var service : serviceList) {
            addNewService(service, javaFileName);
        }
    }

    private void walkTopLevelEnumTypes(List<EnumDescriptor> enumTypeList) {
        for (var enumType : enumTypeList) {
            addNewEnumType(enumType, javaFileName(enumType));
        }
    }

    private void walkTopLevelEnumTypes(List<EnumDescriptor> enumTypeList, String javaFileName) {
        for (var enumType : enumTypeList) {
            addNewEnumType(enumType, javaFileName);
        }
    }

    private void walkTopLevelMessageTypes(List<Descriptor> messageTypeList) {
        for (var messageType : messageTypeList) {
            flatten(messageType, javaFileName(messageType));
        }
    }

    private void walkTopLevelMessageTypes(List<Descriptor> messageTypeList, String javaFileName) {
        for (var messageType : messageTypeList) {
            flatten(messageType, javaFileName);
        }
    }

    private void flatten(Descriptor messageType, String javaFileName) {
        addNewMessageType(messageType, javaFileName);
        walkInnerEnumTypes(messageType.getEnumTypes(), javaFileName);
        walkNestedTypes(messageType.getNestedTypes(), javaFileName);
    }

    private void walkNestedTypes(List<Descriptor> messageTypeList, String javaFileName) {
        for (var messageType : messageTypeList) {
            flatten(messageType, javaFileName);
        }
    }

    private void walkInnerEnumTypes(List<EnumDescriptor> enumTypeList, String javaFileName) {
        for (var enumType : enumTypeList) {
            addNewEnumType(enumType, javaFileName);
        }
    }

    private void addNewService(ServiceDescriptor service, String javaFileName) {
        services.add(
                ProtobufType.newBuilder()
                        .setName(service.getFullName())
                        .setJavaFileName(javaFileName)
                        .setService(service.toProto())
                        .build()
        );
    }

    private void addNewEnumType(EnumDescriptor enumType, String javaFileName) {
        enumTypes.add(
                ProtobufType.newBuilder()
                        .setName(enumType.getFullName())
                        .setJavaFileName(javaFileName)
                        .setEnumType(enumType.toProto())
                        .build()
        );
    }

    private void addNewMessageType(Descriptor messageType, String javaFileName) {
        messageTypes.add(
                ProtobufType.newBuilder()
                        .setName(messageType.getFullName())
                        .setJavaFileName(javaFileName)
                        .setMessageType(messageType.toProto())
                        .build()
        );
    }

    private String javaFileName(FileDescriptor descriptor) {
        var javaPackage = packageName(descriptor);
        var javaSimpleName = descriptor.getOptions().getJavaOuterClassname();
        return relativeFileName(javaPackage, javaSimpleName);
    }

    private String javaFileName(GenericDescriptor descriptor) {
        var javaPackage = packageName(descriptor.getFile());
        var javaSimpleName = descriptor.getName();
        return relativeFileName(javaPackage, javaSimpleName);
    }

    private String packageName(FileDescriptor descriptor){
        return descriptor.getOptions().getJavaPackage();
    }

    private String relativeFileName(String packageName, String javaSimpleName) {
        var javaDirectory = packageName.replaceAll("\\.", "/");
        return format("%s/%s.java", javaDirectory, javaSimpleName);
    }
}
