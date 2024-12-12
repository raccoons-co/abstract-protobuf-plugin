/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import co.raccoons.common.eventbus.Subscribable;
import co.raccoons.protoc.plugin.core.ProtocolFile;
import co.raccoons.protoc.plugin.core.ProtocolFileSet;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * A multipart Java code generator.
 */
public final class CodeGenerator {

    private final ImmutableSet<AbstractCodeGenerator> generators;

    private CodeGenerator(ImmutableSet<AbstractCodeGenerator> generators) {
        this.generators = checkNotNull(generators);
        register();
    }

    /**
     * Processes the given compiler request and generates the Protobuf compiler
     * response files.
     */
    public Collection<File> process(CodeGeneratorRequest request) {
        checkNotNull(request);
        submitEvents(request);
        return extensions();
    }

    /**
     * Obtains a new builder of the {@code CodeGenerator}.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * A builder of the {@code CodeGenerator} instance.
     */
    public static final class Builder {

        private final Set<AbstractCodeGenerator> generators = new HashSet<>();

        private Builder() {
        }

        /**
         * Adds code generator.
         */
        public Builder addGenerator(AbstractCodeGenerator generator) {
            checkNotNull(generator);
            generators.add(generator);
            return this;
        }

        /**
         * Returns a new instance of {@code CodeGenerator}.
         */
        public CodeGenerator build() {
            var immutableGenerators = ImmutableSet.copyOf(generators);
            return new CodeGenerator(immutableGenerators);
        }
    }

    private void register() {
        generators.forEach(Subscribable::register);
    }

    private Collection<File> extensions() {
        return generators.stream()
                .map(AbstractCodeGenerator::extensions)
                .flatMap(Collection::stream)
                .collect(toImmutableSet());
    }

    private static void submitEvents(CodeGeneratorRequest request) {
        var protocolFileSet= ProtocolFileSet.of(request.getProtoFileList());
        request.getFileToGenerateList()
                .stream()
                .map(protocolFileSet::file)
                .forEach(ProtocolFile::walk);
    }
}
