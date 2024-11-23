package co.raccoons.protoc.plugin;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import java.util.Collection;

public class CodeGenerator {

    private CodeGenerator() {
    }

    public Collection<File> process(CodeGeneratorRequest request) {
        return null;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        public CodeGenerator build() {
            return new CodeGenerator();
        }
    }
}
