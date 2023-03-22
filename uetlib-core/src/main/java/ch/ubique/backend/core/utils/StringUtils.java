package ch.ubique.backend.core.utils;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StringUtils {

    private StringUtils() {}

    /**
     * Returns a {@link Stream#empty()} if {@code string == null || size <= 0 || size >
     * string.length()}
     */
    public static Stream<String> substringWindows(final String string, final int size) {
        if (string == null || size <= 0 || size > string.length()) {
            return Stream.empty();
        }
        return IntStream.range(0, string.length() - size + 1)
                .mapToObj(start -> string.substring(start, start + size));
    }

    public static String requireNonBlank(final String obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(obj)) {
            throw new IllegalStateException("must not be blank");
        }
        return obj;
    }

    public static String requireNonBlank(final String obj, final String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(obj)) {
            throw new IllegalStateException(message);
        }
        return obj;
    }

    public static String requireNonBlankElse(final String obj, final String defaultObj) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(obj)
                ? obj
                : requireNonBlank(defaultObj, "defaultObj");
    }

    public static String requireNonBlankElseGet(final String obj, final Supplier<String> supplier) {
        return obj != null
                ? obj
                : requireNonBlank(
                        Objects.requireNonNull(supplier, "supplier").get(), "supplier.get()");
    }
}
