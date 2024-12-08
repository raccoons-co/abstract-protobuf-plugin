/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.core;

import com.google.common.testing.NullPointerTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JavaFileNameTest {

    @Test
    @DisplayName("not accept null")
    void notAcceptNull() {
        new NullPointerTester().testAllPublicStaticMethods(JavaFileName.class);
    }

}