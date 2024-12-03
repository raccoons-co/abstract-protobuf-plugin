package co.raccoons.protoc.plugin.protos;

import co.raccoons.protoc.plugin.ProtobufType.FileName;
import co.raccoons.protoc.plugin.ProtobufTypeSet;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.ServiceDescriptor;

/**
 * Protobuf type collector that generates into single Java file.
 *
 * @see <a href="https://protobuf.dev/reference/java/java-generated/">
 * option java_multiple_files = false;</a>
 */
public class JavaSingleFile extends ProtobufTypeCollector {

    public JavaSingleFile(Descriptors.FileDescriptor protoFile,
                          ProtobufTypeSet.Builder builder) {
        super(protoFile, builder);
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
                .setName(JavaFileName.of(enumType.getFile()).forOuterClass())
                .build();
    }

    @Override
    protected FileName fileNameFor(Descriptor messageType) {
        var protoFile = messageType.getFile();
        var outerClassName = JavaFileName.of(protoFile).forOuterClass();
        return FileName.newBuilder()
                .setName(outerClassName)
                .setBuilderName(outerClassName)
                .setOuterClassName(outerClassName)
                .build();
    }
}
