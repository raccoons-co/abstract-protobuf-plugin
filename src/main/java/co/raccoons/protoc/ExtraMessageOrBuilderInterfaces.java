/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc;

import co.raccoons.protoc.plugin.AbstractCodeGenerator;
import co.raccoons.protoc.plugin.InsertionPoint;
import co.raccoons.protoc.plugin.ProtobufType;
import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

@Immutable
public class ExtraMessageOrBuilderInterfaces extends AbstractCodeGenerator {

    @Override
    protected File generateProtocExtra(ProtobufType type) {
        var insertionPoint = InsertionPoint.interface_extends.forType(type);
        return File.newBuilder()
                .setName(type.getJavaFileName())
                .setInsertionPoint(insertionPoint)
                .setContent("co.raccoons.event.Observable,")
                .build();
    }
}
