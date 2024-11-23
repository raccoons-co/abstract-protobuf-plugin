package co.raccoons.protoc;

import co.raccoons.protoc.plugin.CodeGenerator;
import co.raccoons.protoc.plugin.ProtocPlugin;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse;

public final class Plugin {

    private Plugin() {
    }

    /**
     * The program entry point.
     */
    public static void main(String[] args) {

        new ProtocPlugin() {
            @Override
            public CodeGeneratorResponse response() {

                var generator = CodeGenerator.newBuilder().build();
                var files = generator.process(request());
                return CodeGeneratorResponse.newBuilder()
                        .addAllFile(files)
                        .build();
            }
        }.integrate();
    }
}
