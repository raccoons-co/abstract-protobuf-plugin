/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import co.raccoons.example.Nothing;
import com.google.common.testing.NullPointerTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ProtocolTypeSet")
class ProtocolTypeSetTest {

    @Test
    @DisplayName("not accepts `null`")
    void throwsExceptionOnNull() {
        var protocolTypeSet = ProtocolTypeSet.newBuilder().build();
        new NullPointerTester().testAllPublicInstanceMethods(protocolTypeSet);
    }

    @Test
    @DisplayName("adds message type")
    void addsMessageType() {
        var protocolTypeSet =
                ProtocolTypeSet.newBuilder()
                        .add(Nothing.getDescriptor())
                        .build();
        assertTrue(protocolTypeSet.contains("Nothing"));
    }

    @Test
    @DisplayName("adds enum type")
    void addsEnumType() {
        var protocolTypeSet =
                ProtocolTypeSet.newBuilder()
                        .add(Nothing.NothingEnum.getDescriptor())
                        .build();
        assertTrue(protocolTypeSet.contains("NothingEnum"));
    }
}
