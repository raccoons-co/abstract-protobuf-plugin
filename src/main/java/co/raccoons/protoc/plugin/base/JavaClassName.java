/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.base;

import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;

import static com.google.common.base.Preconditions.checkNotNull;

public class JavaClassName {

    public static final String DOT_SEPARATOR = ".";
    public static final String EMPTY_STRING = "";
    public static final String DOT_REGEX = "\\.";
    public static final String $_SEPARATOR = "\\$";

    private final String value;

    private JavaClassName(String value) {
        this.value = checkNotNull(value);
    }

    public static JavaClassName of(JavaPackageName javaPackageName,
                                   String javaSimpleName) {
        checkNotNull(javaPackageName);
        checkNotNull(javaSimpleName);
        var javaFullName = javaPackageName.value() + DOT_SEPARATOR + javaSimpleName;
        return new JavaClassName(javaFullName);
    }

    /**
     * Obtains an instance of {@code JavaClassName} from given Protocol message
     * type.
     */
    public static JavaClassName from(FileDescriptor descriptor) {
        checkNotNull(descriptor);
        var javaPackageName = JavaPackageName.from(descriptor);
        var javaName = withNestedNames(descriptor);
        return JavaClassName.of(javaPackageName, javaName);
    }

    /**
     * Returns java class name value.
     */
    public String value() {
        return value;
    }

    private static <T extends GenericDescriptor> String withNestedNames(T descriptor){
        var protoPackage = protoPackage(descriptor.getFile());
        return descriptor.getFullName()
                .replace(protoPackage + DOT_SEPARATOR, EMPTY_STRING)
                .replaceAll(DOT_REGEX, $_SEPARATOR);
    }

    private static String protoPackage(FileDescriptor protoFile) {
        return protoFile.getPackage();
    }
}
