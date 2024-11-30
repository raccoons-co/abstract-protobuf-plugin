package co.raccoons.protoc.plugin.protofile;

import com.google.protobuf.Descriptors.FileDescriptor;

public final class JavaMultipleFile extends ProtoFileWalker {

    @Override
    protected void handle(ProtoFileVisitor visitor, FileDescriptor protoFile) {
        visitor.visit(this, protoFile);
    }
}
