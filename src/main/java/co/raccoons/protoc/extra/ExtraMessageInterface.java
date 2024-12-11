/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.extra;

import co.raccoons.protoc.OptionsProto;
import co.raccoons.protoc.plugin.AbstractCodeGenerator;
import co.raccoons.protoc.plugin.ProtocolType;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

/**
 * An extra message interface code generator.
 * <p>
 * The interface type is defined by custom option `extra`.
 * <p>
 * Example:
 * <pre>
 * message MyEvent {
 *   option (extra).message_implements = "co.raccoons.eventbus.Observable";
 *   string name = 1;
 * }
 * </pre>
 */
final class ExtraMessageInterface extends AbstractCodeGenerator {

    @Override
    protected Predicate<ProtocolType> filter() {
        return super.filter()
                .and(ExtraMessageInterface::hasMessageType)
                .and(ExtraMessageInterface::hasExtraOption);
    }

    @Override
    protected File generate(ProtocolType protocolType) {
        var type = protocolType.getProtobufType();
        var insertionPoint = ProtocExtra.message_implements.newInsertionPoint(type);
        var content = content(protocolType);

        return File.newBuilder()
                .setName(insertionPoint.getFileName())
                .setInsertionPoint(insertionPoint.getIdentifier())
                .setContent(content)
                .build();
    }

    private String content(ProtocolType protocolType) {
        var messageImplements = protocolType.getMessageType()
                .getOptions()
                .getExtension(OptionsProto.extra)
                .getMessageImplements();
        checkArgument(!isNullOrEmpty(messageImplements));
        return format("%s,", messageImplements);
    }

    private static boolean hasMessageType(ProtocolType protocolType) {
        return protocolType.hasMessageType();
    }

    private static boolean hasExtraOption(ProtocolType protocolType) {
        return protocolType.getMessageType()
                .getOptions()
                .hasExtension(OptionsProto.extra);
    }
}
