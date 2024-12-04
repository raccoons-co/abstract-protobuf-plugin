/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.protos;

import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * A relative java file name of the given protocol message type.
 *
 * @param <T> the protocol message type
 */
@Immutable(containerOf = "T")
final class JavaFileName<T extends GenericDescriptor> {

    private static final String CLASS_PATTERN = "%s/%s.java";
    private static final String MESSAGE_OR_BUILDER_PATTERN = "%s/%sOrBuilder.java";
    private static final String NO_OUTER_CLASS_PATTERN = "%s/%sOuterClass.java";

    private final T descriptor;

    private JavaFileName(T descriptor) {
        this.descriptor = checkNotNull(descriptor);
    }

    /**
     * Returns new instance of {@code JavaFileName} of the given protocol
     * message type.
     */
    public static <T extends GenericDescriptor> JavaFileName<T> of(T descriptor) {
        return new JavaFileName<>(descriptor);
    }

    /**
     * Obtains relative java file name for Message type class.
     */
    public String forClass() {
        return format(CLASS_PATTERN, directory(), simpleName());
    }

    /**
     * Obtains relative java file name for MessageOrBuilder type class.
     */
    public String forMessageOrBuilder() {
        return format(MESSAGE_OR_BUILDER_PATTERN, directory(), simpleName());
    }

    /**
     * Obtains relative java file name for Outer class.
     */
    public String forOuterClass() {
        return hasJavaOuterClassname()
                ? format(CLASS_PATTERN, directory(), javaOuterClassName())
                : format(NO_OUTER_CLASS_PATTERN, directory(), simpleName());
    }

    private String directory() {
        return javaPackage().replaceAll("\\.", "/");
    }

    private String simpleName() {
        var name = descriptor.getName();
        return (descriptor instanceof FileDescriptor)
                ? fromProtoFileName(name)
                : name;
    }

    private String fromProtoFileName(String name) {
        return capitalize(name).replaceAll("\\.proto$", "");
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String javaPackage() {
        return descriptor.getFile().getOptions().getJavaPackage();
    }

    private boolean hasJavaOuterClassname() {
        return descriptor.getFile().getOptions().hasJavaOuterClassname();
    }

    private String javaOuterClassName() {
        return descriptor.getFile().getOptions().getJavaOuterClassname();
    }
}
