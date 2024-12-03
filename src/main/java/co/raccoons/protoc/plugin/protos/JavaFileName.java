/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.protos;

import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * A relative java file name of the given protocol message type.
 *
 * @param <T> the protocol message type
 */
final class JavaFileName<T extends GenericDescriptor> {

    private static final String CLASS_PATTERN = "%s/%s.java";
    private static final String BUILDER_PATTERN = "%s/%sOrBuilder.java";
    private static final String EMPTY_OUTER_CLASS_PATTERN = "%s/%sOuterClass.java";

    private final T descriptor;

    private JavaFileName(T descriptor) {
        this.descriptor = checkNotNull(descriptor);
    }

    /**
     * Returns new instance of {@code JavaFileName} ot the given protocol
     * message type.
     */
    public static <T extends GenericDescriptor> JavaFileName<T> of(T descriptor) {
        return new JavaFileName<T>(descriptor);
    }

    /**
     * Obtains relative java file name for message type class.
     */
    public String forClass() {
        return format(CLASS_PATTERN, directory(), simpleName());
    }

    /**
     * Obtains relative java file name for message or builder type class.
     */
    public String forBuilder() {
        return format(BUILDER_PATTERN, directory(), simpleName());
    }

    /**
     * Obtains relative java file name for type outer class.
     */
    public String forOuterClass() {
        return hasJavaOuterClassname()
                ? format(CLASS_PATTERN, directory(), outerClassName())
                : format(EMPTY_OUTER_CLASS_PATTERN, directory(), simpleName());
    }

    private String directory() {
        return descriptor.getFile()
                .getOptions()
                .getJavaPackage()
                .replaceAll("\\.", "/");
    }

    private String simpleName() {
        var name = descriptor.getName();
        return (descriptor instanceof FileDescriptor)
                ? capitalize(name).replaceAll("\\.proto$", "")
                : name;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private boolean hasJavaOuterClassname() {
        return descriptor.getFile().getOptions().hasJavaOuterClassname();
    }

    private String outerClassName() {
        return descriptor.getFile().getOptions().getJavaOuterClassname();
    }
}
