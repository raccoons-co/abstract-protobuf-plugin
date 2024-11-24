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

    /**
     * Extra MessageOrBuilder Interfaces.
     */
    interface_extends,

    class_scope,

    builder_scope,

    enum_scope,

    outer_class_scope {
        @Override
        public String forType(ProtobufType type) {
            return name();
        }
    };

    /**
     * Returns insertion point string representation for the given type name.
     */
    public String forType(ProtobufType type) {
        return format("%s:%s", name(), type.getName());
    }
}
