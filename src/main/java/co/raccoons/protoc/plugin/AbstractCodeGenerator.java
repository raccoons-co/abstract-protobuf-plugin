/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides a skeletal implementation of the Protobuf to Java code
 * generator.
 *
 * <p>
 * To implement a concrete code generator, the programmer needs to extend this
 * class and provide implementation for the {@code generateProtocExtra(...)}
 * method.
 * <p>
 * If a concrete code generator intended to process a Protobuf services
 * or enum types the {@code typeScope()} method needs to be overridden as well.
 *
 * @see <a href="https://github.com/protocolbuffers/protobuf/blob/main/src/google/protobuf/compiler/plugin.proto">
 * plugin.proto#</a>
 */
public abstract class AbstractCodeGenerator {

    /**
     * Processes the given compiler request and generates the Protobuf compiler
     * extra response files.
     */
    public final Collection<File> process(CodeGeneratorRequest request) {
        checkNotNull(request);
        var extendableTypes = types(request);
        var protoTypes = typeScope().limit(extendableTypes);
        return extensions(protoTypes);
    }

    /**
     * Generates Protobuf extra artifact represented by
     * {@code CodeGeneratorResponse.File} that adds code to the insertion points.
     */
    protected abstract File generateProtocExtra(ProtobufType type);

    /**
     * Defines the scope of Protobuf types that are processed by the code
     * generator.
     * <p>
     * Designed to be overridden if a concrete code generator should to process
     * Protobuf services or enum types.
     */
    protected ProtobufTypeScope typeScope() {
        return ProtobufTypeScope.MESSAGE;
    }

    private ProtobufTypeSet types(CodeGeneratorRequest request) {
        return ProtobufFileSet.of(request.getProtoFileList())
                .newProtobufTypeSet(request.getFileToGenerateList());
    }

    private Collection<File> extensions(Collection<ProtobufType> extendableTypes) {
        return extendableTypes
                .stream()
                .map(this::generateProtocExtra)
                .collect(Collectors.toList());
    }

    /**
     * An enumeration that limits the type set of Protobuf types that are
     * processed by the code generator.
     *
     * @see AbstractCodeGenerator#typeScope()
     */
    protected enum ProtobufTypeScope {

        MESSAGE {
            @Override
            public Collection<ProtobufType> limit(ProtobufTypeSet typeSet) {
                return typeSet.getMessageTypeList();
            }
        },
        ENUM {
            @Override
            public Collection<ProtobufType> limit(ProtobufTypeSet typeSet) {
                return typeSet.getEnumTypeList();
            }
        },
        SERVICE {
            @Override
            public Collection<ProtobufType> limit(ProtobufTypeSet typeSet) {
                return typeSet.getServiceList();
            }
        };

        public abstract Collection<ProtobufType> limit(ProtobufTypeSet typeSet);
    }
}
