package co.raccoons.protoc.plugin;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AbstractProtocPlugin")
class AbstractProtocPluginTest {

    @Test
    @DisplayName("throws exception")
    void throwsOnNullResponse() {
        var exception =
                assertThrows(NullPointerException.class,
                        () ->
                                new AbstractProtocPlugin() {
                                    @Override
                                    protected CodeGeneratorResponse response() {
                                        return null;
                                    }
                                }.integrate()
                );

        assertEquals("CodeGeneratorResponse is null.", exception.getMessage());
    }

    @Test
    @DisplayName("writes to stdout")
    void writesToSystemOut() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        new AbstractProtocPlugin() {
            @Override
            protected CodeGeneratorResponse response() {
                return CodeGeneratorResponse.newBuilder()
                        .addFile(file)
                        .build();
            }
        }.integrate();

        assertTrue(outputStream.toString().contains("ExampleMessage.java"));
        assertTrue(outputStream.toString().contains("message_implements:"));
        assertTrue(outputStream.toString().contains("co.raccoons.event.Observable,"));
    }

    @Test
    @DisplayName("tries to read stdin")
    void readsFromSystemIn() {
        System.setIn(new ByteArrayInputStream("test".getBytes()));
        var exception =
                assertThrows(IllegalStateException.class,
                        () ->
                                new AbstractProtocPlugin() {
                                    @Override
                                    protected CodeGeneratorResponse response() {
                                        var unused = request();
                                        return null;
                                    }
                                }.integrate()
                );
        assertEquals("Unable to read code generator request.", exception.getMessage());
    }

    private final File file =
            File.newBuilder()
                    .setName("ExampleMessage.java")
                    .setInsertionPoint("message_implements:")
                    .setContent("co.raccoons.event.Observable,")
                    .build();
}