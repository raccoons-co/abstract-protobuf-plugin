/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.extra;

import co.raccoons.protoc.plugin.InsertionPoint;
import co.raccoons.protoc.plugin.ProtobufType;

import static java.lang.String.format;

/**
 * Represents extension of the code generator output.
 *
 * @see <a href="https://protobuf.dev/reference/java/java-generated/#plugins">
 * Plugin Insertion Points</a>
 */
public enum ProtocExtra {

    /**
     * Extra interface of the Message type class.
     */
    message_implements,

    /**
     * Extra interface of the Builder class.
     */
    builder_implements,

    /**
     * Extra code of the Message type class.
     */
    class_scope,

    /**
     * Extra code of Builder class.
     */
    builder_scope,

    /**
     * Extra code of Enum.
     */
    enum_scope,

    /**
     * Extra interface of the MessageOrBuilder interface.
     */
    interface_extends {
        @Override
        protected String javaFileName(ProtobufType type) {
            return type.getJavaFileName().getMessageOrBuilderName();
        }
    },

    /**
     * Extra code of wrapper class.
     */
    outer_class_scope {
        @Override
        protected String javaFileName(ProtobufType type) {
            return type.getJavaFileName().getOuterClassName();
        }

        @Override
        protected String identifier(ProtobufType type) {
            return format("%s", name());
        }
    };

    /**
     * Obtains a new instance of the {@code InsertionPoint} for the given type.
     */
    public InsertionPoint newInsertionPoint(ProtobufType type) {
        return InsertionPoint.newBuilder()
                .setFileName(javaFileName(type))
                .setIdentifier(identifier(type))
                .build();
    }

    /**
     * Returns insertion point identifier of the given type.
     */
    protected String identifier(ProtobufType type) {
        return format("%s:%s", name(), type.getName());
    }

    protected String javaFileName(ProtobufType type) {
        return type.getJavaFileName().getName();
    }
}
