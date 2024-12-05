/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.extra;

import co.raccoons.protoc.plugin.AbstractCodeGenerator;
import co.raccoons.protoc.plugin.ProtobufType;
import co.raccoons.protoc.plugin.ProtocExtra;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

@Immutable
public class ExtraMessageOrBuilderInterface extends AbstractCodeGenerator {

    @Override
    protected File generate(ProtobufType type) {
        var insertionPoint = ProtocExtra.interface_extends.newInsertionPoint(type);
        return File.newBuilder()
                .setName(insertionPoint.getFileName())
                .setInsertionPoint(insertionPoint.getIdentifier())
                .setContent("co.raccoons.event.Observable,")
                .build();
    }
}
