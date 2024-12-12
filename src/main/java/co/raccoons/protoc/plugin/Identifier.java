/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import static java.lang.String.format;

public enum Identifier {

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
    interface_extends,

    /**
     * Extra code of wrapper class.
     */
    outer_class_scope {
        @Override
        public String forType(ProtocolType protocolType) {
            return format("%s", name());
        }
    };

    /**
     * Returns insertion point identifier of the given protocolType.
     */
    public String forType(ProtocolType protocolType) {
        return format("%s:%s", name(), protocolType.getName());
    }
}
