/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.base;

import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * A java package name.
 */
public class JavaPackageName {

    private final String value;

    private JavaPackageName(String value) {
        this.value = checkNotNull(value);
    }

    /**
     * Obtains an instance of {@code JavaPackageName} of given value.
     */
    public static JavaPackageName of(String value) {
        checkArgument(!value.isBlank(), "Package name is undefined");
        return new JavaPackageName(value);
    }

    /**
     * Obtains an instance of {@code JavaPackageName} from given Protocol
     * message type.
     */
    public static <T extends GenericDescriptor> JavaPackageName from(T descriptor) {
        checkNotNull(descriptor);
        var value = javaPackage(descriptor.getFile());
        return JavaPackageName.of(value);
    }

    /**
     * Returns java package name value.
     */
    public String value() {
        return value;
    }

    private static String javaPackage(FileDescriptor protoFile) {
        var javaPackage = protoFile.getOptions().getJavaPackage();
        return isNullOrEmpty(javaPackage)
                ? protoFile.getPackage()
                : javaPackage;
    }
}
