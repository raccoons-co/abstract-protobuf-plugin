/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides a skeletal implementation of the Protobuf compiler plugin
 * interface to minimize the effort required to implement this interface.
 * <p>
 * To implement a concrete protoc plugin, the programmer needs to extend this
 * class and provide implementation for the {@code response()} method.
 */
@Immutable
public abstract class AbstractProtocPlugin {
    /**
     * Sole constructor. (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractProtocPlugin() {
    }

    /**
     * Integrates generated code to Protobuf compiler.
     */
    public final void integrate() {
        write(this::pluginOutput);
    }

    /**
     * Returns generated {@code CodeGeneratorResponse} for this plugin.
     * <p>
     * This method is intended to implement a concrete Protobuf compiler plugin.
     */
    protected abstract CodeGeneratorResponse response();

    /**
     * Obtains an encoded {@code CodeGeneratorRequest} that written to
     * the plugin's stdin.
     */
    protected final CodeGeneratorRequest request() {
        return read(this::pluginInput);
    }

    /**
     * Reads an encoded {@code CodeGeneratorRequest} that written to the plugin's
     * stdin.
     */
    private CodeGeneratorRequest read(Supplier<CodeGeneratorRequest> stdin) {
        return stdin.get();
    }

    /**
     * Writes an encoded {@code CodeGeneratorResponse} to stdout.
     */
    private void write(Consumer<CodeGeneratorResponse> stdout) {
        stdout.accept(response());
    }

    private CodeGeneratorRequest pluginInput() {
        try {
            return CodeGeneratorRequest.parseFrom(System.in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read code generator request.", e);
        }
    }

    private void pluginOutput(CodeGeneratorResponse response) {
        try {
            checkNotNull(response, "CodeGeneratorResponse is null.");
            response.writeTo(System.out);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write code generator response.", e);
        }
    }
}
