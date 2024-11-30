package co.raccoons.protoc.plugin.protofile;

import com.google.protobuf.Descriptors.FileDescriptor;

public interface ProtoFileVisitor {

    void visit(JavaMultipleFile visitor, FileDescriptor protoFile);

    void visit(JavaSingleFile visitor, FileDescriptor protoFile);
}
