package co.raccoons.protoc.plugin.protofile;

import com.google.protobuf.Descriptors.FileDescriptor;

public class JavaSingleFile extends ProtoFileWalker {

    @Override
    protected void handle(ProtoFileVisitor visitor, FileDescriptor protoFile) {
        visitor.visit(this, protoFile);
    }
}
