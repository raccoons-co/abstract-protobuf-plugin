package co.raccoons.protoc.plugin.protofile;

import co.raccoons.protoc.plugin.ProtobufTypeFileName;
import co.raccoons.protoc.plugin.ProtobufTypeSet;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

public class JavaSingleFile extends ProtobufTypeCollector {

    public JavaSingleFile(Descriptors.FileDescriptor protoFile,
                          ProtobufTypeSet.Builder builder) {
        super(protoFile, builder);
    }

    @Override
    protected ProtobufTypeFileName fileNameFor(ServiceDescriptor service) {
        return ProtobufTypeFileName.newBuilder()
                .setName(JavaFileName.of(service.getFile()).forOuterClass())
                .build();
    }

    @Override
    protected ProtobufTypeFileName fileNameFor(EnumDescriptor enumType) {
        return ProtobufTypeFileName.newBuilder()
                .setName(JavaFileName.of(enumType.getFile()).forOuterClass())
                .build();
    }

    @Override
    protected ProtobufTypeFileName fileNameFor(Descriptor messageType) {
        var outerClassName = JavaFileName.of(messageType.getFile()).forOuterClass();
        return ProtobufTypeFileName.newBuilder()
                .setName(outerClassName)
                .setBuilderName(outerClassName)
                .setOuterClassName(outerClassName)
                .build();
    }
}
