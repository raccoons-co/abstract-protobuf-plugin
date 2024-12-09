/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import co.raccoons.eventbus.Subscribable;
import com.google.common.eventbus.Subscribe;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class has skeletal implementation to handle extra java code generation
 * for any Protocol Buffer message types.
 * <p>
 * To introduce a concrete code generator the programmer must extend this class
 * and provide implementation for the method {@code generate(...)}.
 */
public abstract class AbstractCodeGenerator implements Subscribable {

    private final Set<File> extensions = new HashSet<>();

    /**
     * Returns extensions that adds extra java code.
     */
    public final Collection<File> extensions() {
        return extensions;
    }

    /**
     * Generates code generator response file that extends the output produced
     * by another code generator for any protocol message type.
     */
    protected abstract File generate(ProtocolType protocolType);

    /**
     * Handles extra java code generation for any Protocol message types.
     */
    @Subscribe
    @SuppressWarnings("ReturnValueIgnored")
    protected final void handle(ProtocolType protocolType) {
        checkNotNull(protocolType);
        filter().and(this::addGeneratedFile).test(protocolType);
    }

    /**
     * This method is designed to be overridden. The programmer can filter any
     * protocol message type that must be processed by the concrete generator.
     * <p>
     * If multiple predicates should be composed the method must call
     * {@code super.filter()} first.
     * <p>
     * Example:
     * <pre>
     * protected Predicate&lt;ProtocolType&gt; filter() {
     *     return super.filter()
     *         .and(ExtraMessageInterface::hasMessageType)
     *         .and(ExtraMessageInterface::hasExtraOption);
     * }
     * </pre>
     */
    protected Predicate<ProtocolType> filter() {
        return this::alwaysTrue;
    }

    private boolean addGeneratedFile(ProtocolType protocolType) {
        var file = generate(protocolType);
        return extensions.add(file);
    }

    private boolean alwaysTrue(ProtocolType ignored) {
        return true;
    }
}
