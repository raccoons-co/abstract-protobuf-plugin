/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.extra;

import co.raccoons.protoc.OptionsProto;
import co.raccoons.protoc.plugin.AbstractProtocPlugin;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse;

/**
 * The Protobuf Compiler plugin.
 */
public final class Plugin {

    private Plugin() {
    }

    /**
     * The program entry point.
     */
    public static void main(String[] args) {
        new AbstractProtocPlugin() {
            @Override
            protected void registerCustomOptions(ExtensionRegistry registry) {
                OptionsProto.registerAllExtensions(registry);
            }

            @Override
            public CodeGeneratorResponse response() {
                var request = request();
                var messageInterfaces = new ExtraMessageInterface().process(request);
                var messageOrBuilderInterfaces = new ExtraMessageOrBuilderInterface().process(request);
                return CodeGeneratorResponse.newBuilder()
                        .addAllFile(messageInterfaces)
                        .addAllFile(messageOrBuilderInterfaces)
                        .build();
            }
        }.integrate();
    }
}
