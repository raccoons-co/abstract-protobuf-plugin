package co.raccoons.protoc.plugin.protofile;

import com.google.protobuf.Descriptors.FileDescriptor;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ProtoFileWalker {

    public final void accept(ProtoFileVisitor visitor, FileDescriptor protoFile){
        checkNotNull(visitor);
        checkNotNull(protoFile);
        handle(visitor, protoFile);
    }

    protected abstract void handle(ProtoFileVisitor visitor, FileDescriptor protoFile);
}
