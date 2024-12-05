/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin;

import com.google.errorprone.annotations.Immutable;

import static java.lang.String.format;

/**
 * Utility class that helps to build insertion point content.
 */
@Immutable
public final class Content {

    private Content(){
    }

    public static final String SEPARATOR = ",";

    /**
     * Returns string with class name and separator that should be used as a
     * content for the insertion point which implements or extends types.
     */
    public static <T> String inheritanceOf(Class<T> tClass) {
        return format("%s%s", tClass.getName(), SEPARATOR);
    }
}
