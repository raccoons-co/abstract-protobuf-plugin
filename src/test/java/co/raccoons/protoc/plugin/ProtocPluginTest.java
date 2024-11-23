package co.raccoons.protoc.plugin;

import com.google.protobuf.compiler.PluginProtos;
import org.junit.jupiter.api.Test;

class ProtocPluginTest {

    @Test
    void nothing(){
        new ProtocPlugin() {
            @Override
            public PluginProtos.CodeGeneratorResponse response() {
                return null;
            }
        };
    }

}