package co.raccoons.protoc.plugin.protofile;

import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.Descriptors.FileDescriptor;

import static com.google.common.base.Preconditions.checkNotNull;

public final class JavaOptionPreset {

    private final FileOptions options;

    public JavaOptionPreset(FileOptions options) {
        this.options = checkNotNull(options);
    }

    public static JavaOptionPreset of(FileDescriptor protoFile) {
        checkNotNull(protoFile);
        var options = protoFile.getOptions();
        return new JavaOptionPreset(options);
    }

    public boolean isJavaGenericServices() {
        return options.getJavaGenericServices();
    }

    public ProtoFileWalker multipleFilesOrSingle() {
        return options.getJavaMultipleFiles()
                ? new JavaMultipleFile()
                : new JavaSingleFile();
    }
}
