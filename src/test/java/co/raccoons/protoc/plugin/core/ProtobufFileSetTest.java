/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import com.google.common.testing.NullPointerTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProtobufFileSet")
class ProtobufFileSetTest {

    @Test
    @DisplayName("not accept null")
    void notAcceptNull(){
        var tester  = new NullPointerTester();
        tester.testAllPublicStaticMethods(ProtobufFileSet.class);
    }
}