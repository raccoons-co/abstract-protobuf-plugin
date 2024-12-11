package co.raccoons.protoc.plugin.core;

import co.raccoons.protoc.plugin.ProtobufType.FileName;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

/**
 * Protobuf type collector that generates single Java file.
 *
 * @see <a href="https://protobuf.dev/reference/java/java-generated/">
 * option java_multiple_files = false;</a>
 */
final class JavaSingleFile extends ProtobufTypeWalker {

    public JavaSingleFile(FileDescriptor protoFile) {
        super(protoFile);
    }

    @Override
    protected FileName fileNameFor(ServiceDescriptor service) {
        var protoFile = service.getFile();
        return FileName.newBuilder()
                .setName(JavaFileName.of(protoFile).forOuterClass())
                .build();
    }

    @Override
    protected FileName fileNameFor(EnumDescriptor enumType) {
        var protoFile = enumType.getFile();
        return FileName.newBuilder()
                .setName(JavaFileName.of(protoFile).forOuterClass())
                .build();
    }

    @Override
    protected FileName fileNameFor(Descriptor messageType) {
        var protoFile = messageType.getFile();
        var outerClassName = JavaFileName.of(protoFile).forOuterClass();
        return FileName.newBuilder()
                .setName(outerClassName)
                .setMessageOrBuilderName(outerClassName)
                .setOuterClassName(outerClassName)
                .build();
    }

    @Override
    protected FileName fileNameForInner(Descriptor messageType) {
        var outerClassName = JavaFileName.of(messageType).forOuterClass();
        return FileName.newBuilder()
                .setName(outerClassName)
                .setMessageOrBuilderName(outerClassName)
                .setOuterClassName(outerClassName)
                .build();
    }
}
