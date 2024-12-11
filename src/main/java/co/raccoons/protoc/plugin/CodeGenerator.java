/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import co.raccoons.eventbus.Subscribable;
import co.raccoons.protoc.plugin.core.ProtobufFileSet;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides a skeletal implementation of the Protobuf to Java code
 * generator to minimize the effort required to implement this interface.
 *
 *
 * @see <a href="https://github.com/protocolbuffers/protobuf/blob/main/src/google/protobuf/compiler/plugin.proto">
 * plugin.proto#</a>
 */
public final class CodeGenerator {

    private final ImmutableSet<AbstractCodeGenerator> handlers;

    private CodeGenerator(ImmutableSet<AbstractCodeGenerator> handlers) {
        this.handlers = checkNotNull(handlers);
        register();
    }

    /**
     * Processes the given compiler request and generates the Protobuf compiler
     * extra response files.
     */
    public Collection<File> process(CodeGeneratorRequest request) {
        checkNotNull(request);
        ProtobufFileSet.of(request.getProtoFileList())
                .runCollector(request.getFileToGenerateList());

        return extensions();
    }

    /**
     * Obtains a new builder of the {@code CodeGenerator}.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private final Set<AbstractCodeGenerator> handlers = new HashSet<>();

        private Builder() {
        }

        /**
         * Adds code generator.
         */
        public Builder addGenerator(AbstractCodeGenerator generator) {
            checkNotNull(generator);
            handlers.add(generator);
            return this;
        }

        /**
         * Returns a new instance of {@code CodeGenerator}.
         */
        public CodeGenerator build() {
            var immutableSet = ImmutableSet.copyOf(handlers);
            return new CodeGenerator(immutableSet);
        }
    }

    private void register() {
        handlers.forEach(Subscribable::register);
    }

    private Collection<File> extensions() {
        return handlers.stream()
                .map(AbstractCodeGenerator::extensions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
