/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc;

import co.raccoons.protoc.plugin.AbstractProtocPlugin;
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
            public CodeGeneratorResponse response() {
                var request = request();
                var generator = new ExtraMessageInterface();
                var files = generator.process(request);
                return CodeGeneratorResponse.newBuilder()
                        .addAllFile(files)
                        .build();
            }
        }.integrate();
    }
}
