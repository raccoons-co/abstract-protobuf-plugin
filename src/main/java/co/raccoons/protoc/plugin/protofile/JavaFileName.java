/*
 * Copyright 2024, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.protoc.plugin.protofile;

import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

final class JavaFileName<T extends GenericDescriptor> {

    private static final String CLASS_PATTERN = "%s/%s.java";
    private static final String BUILDER_PATTERN = "%s/%sOrBuilder.java";
    private static final String EMPTY_OUTER_CLASS_PATTERN = "%s/%sOuterClass.java";

    private final T descriptor;

    private JavaFileName(T descriptor) {
        this.descriptor = checkNotNull(descriptor);
    }

    public static <T extends GenericDescriptor> JavaFileName<T> of(T descriptor) {
        return new JavaFileName<T>(descriptor);
    }

    public String forClass() {
        return format(CLASS_PATTERN, directory(), simpleName());
    }

    public String forBuilder() {
        return format(BUILDER_PATTERN, directory(), simpleName());
    }

    public String forOuterClass() {
        if (hasJavaOuterClassname()) {
            return format(CLASS_PATTERN, directory(), outerClassName());
        } else {
            return format(EMPTY_OUTER_CLASS_PATTERN, directory(), simpleName());
        }
    }

    private boolean hasJavaOuterClassname() {
        return descriptor.getFile().getOptions().hasJavaOuterClassname();
    }

    private String directory() {
        return descriptor.getFile()
                .getOptions()
                .getJavaPackage()
                .replaceAll("\\.", "/");
    }

    private String outerClassName() {
        return descriptor.getFile().getOptions().getJavaOuterClassname();
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
}
