/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.extra;

import co.raccoons.protoc.plugin.AbstractCodeGenerator;
import co.raccoons.protoc.plugin.ProtobufType;
import co.raccoons.protoc.plugin.ProtocExtra;
import com.google.common.eventbus.EventBus;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import static co.raccoons.protoc.plugin.Content.inheritanceOf;

@Immutable
public class ExtraMessageInterface extends AbstractCodeGenerator {

    @Override
    protected File generate(ProtobufType type) {
        var insertionPoint = ProtocExtra.message_implements.newInsertionPoint(type);
        return File.newBuilder()
                .setName(insertionPoint.getFileName())
                .setInsertionPoint(insertionPoint.getIdentifier())
                .setContent(inheritanceOf(EventBus.class))
                .build();
    }
}
