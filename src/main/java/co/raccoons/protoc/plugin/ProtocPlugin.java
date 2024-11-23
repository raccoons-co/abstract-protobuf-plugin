package co.raccoons.protoc.plugin;

import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract Protobuf compiler plugin.
 */
@Immutable
public abstract class ProtocPlugin {

    /**
     * Integrates Protobuf compiler plugin
     */
    public final void integrate() {
        write(StandardStream::pluginOutput);
    }

    /**
     * Returns generated {@code CodeGeneratorResponse}.
     *
     * This method is intended to implement a concrete Protobuf compiler plugin.
     */
    protected abstract CodeGeneratorResponse response();

    /**
     * Obtains an encoded {@code CodeGeneratorRequest} that written to the plugin's stdin.
     */
    protected final CodeGeneratorRequest request() {
        return read(StandardStream::pluginInput);
    }

    /**
     * Reads an encoded {@code CodeGeneratorRequest} that written to the plugin's stdin.
     */
    private CodeGeneratorRequest read(Supplier<CodeGeneratorRequest> stdin) {
        checkNotNull(stdin);
        return stdin.get();
    }

    /**
     * Writes an encoded {@code CodeGeneratorResponse} to stdout.
     */
    private void write(Consumer<CodeGeneratorResponse> stdout) {
        checkNotNull(stdout);
        stdout.accept(response());
    }

    private final static class StandardStream {

        private static CodeGeneratorRequest pluginInput() {
            try {
                return CodeGeneratorRequest.parseFrom(System.in);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        private static void pluginOutput(CodeGeneratorResponse response) {
            try {
                response.writeTo(System.out);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }
}
