/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import co.raccoons.protoc.OptionsProto;
import com.google.common.testing.NullPointerTester;
import com.google.protobuf.ExtensionRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("CustomOptionRegistry")
class CustomOptionRegistryTest {

    @Test
    @DisplayName("not accept `null`")
    void throwsNullPointerException(){
        new NullPointerTester().testAllPublicStaticMethods(CustomOptionRegistry.class);
    }

    @Test
    @DisplayName("is instance of ExtensionRegistry")
    void returnsCorrectType() {
        var registry = CustomOptionRegistry.newRegistry(this::register);
        assertInstanceOf(ExtensionRegistry.class, registry);
    }

    @Test
    @DisplayName("has registered custom option")
    void hasRegisteredCustomOption() {
        var registry = CustomOptionRegistry.newRegistry(this::register);
        var customOption = registry.findImmutableExtensionByName("extra");
        assertEquals("extra", customOption.descriptor.getName());
    }

    private void register(ExtensionRegistry registry) {
        OptionsProto.registerAllExtensions(registry);
    }
}