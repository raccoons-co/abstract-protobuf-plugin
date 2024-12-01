/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import static java.lang.String.format;

/**
 * Represents insertion point names.
 *
 * @see <a href="https://protobuf.dev/reference/java/java-generated/#plugins">
 * Plugin Insertion Points</a>
 */
public enum InsertionPoint {

    /**
     * Extra Message Interfaces.
     */
    message_implements,

    /**
     * Extra Builder Interfaces.
     */
    builder_implements,

    class_scope,

    builder_scope,

    enum_scope,

    /**
     * Extra MessageOrBuilder Interfaces.
     */
    interface_extends {
        @Override
        protected String relativeFileName(ProtobufType type) {
            return type.getJavaFileName().getBuilderName();
        }
    },


    outer_class_scope {
        @Override
        protected String relativeFileName(ProtobufType type) {
            return type.getJavaFileName().getOuterClassName();
        }

        @Override
        protected String forType(ProtobufType type) {
            return name();
        }
    };

    public ProtocExtra newProtocExtra(ProtobufType type) {
        return ProtocExtra.newBuilder()
                .setFileName(relativeFileName(type))
                .setInsertionPoint(forType(type))
                .build();
    }

    /**
     * Returns insertion point string representation for the given type name.
     */
    protected String forType(ProtobufType type) {
        return format("%s:%s", name(), type.getName());
    }

    protected String relativeFileName(ProtobufType type) {
        return type.getJavaFileName().getName();
    }
}
