package ch.ubique.backend.core.utils;

import org.jetbrains.annotations.Contract;

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

    @Contract("null -> fail")
    public static String requireNonBlank(final String obj) {
        Objects.requireNonNull(obj);
        if (org.apache.commons.lang3.StringUtils.isBlank(obj)) {
            throw new IllegalStateException("must not be blank");
        }
        return obj;
    }

    @Contract("null, _ -> fail")
    public static String requireNonBlank(final String obj, final String message) {
        Objects.requireNonNull(obj, message);
        if (org.apache.commons.lang3.StringUtils.isBlank(obj)) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    @Contract("null, null -> fail")
    public static String requireNonBlankElse(final String obj, final String defaultObj) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(obj)
                ? obj
                : requireNonBlank(defaultObj, "defaultObj");
    }

    @Contract("null, null -> fail")
    public static String requireNonBlankElseGet(final String obj, final Supplier<String> supplier) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(obj)
                ? obj
                : requireNonBlank(
                        Objects.requireNonNull(supplier, "supplier").get(), "supplier.get()");
    }

    /**
     * normalizes the String by replacing whitespace by a normal space. Uses {@link
     * org.apache.commons.lang3.StringUtils#normalizeSpace} but additionally replaces non-breaking
     * spaces {@code   (\u00A0)} by spaces.
     */
    @Contract("null -> null")
    public static String normalizeSpaceAndNonBreakingSpace(final String input) {
        // \u00A0 is non-breaking space
        final var nonBreakingSpaceReplaced = input == null ? null : input.replace('\u00A0', ' ');
        return org.apache.commons.lang3.StringUtils.normalizeSpace(nonBreakingSpaceReplaced);
    }

    /**
     * normalizes the String using {@link #normalizeSpaceAndNonBreakingSpace} and strip whitespace
     * using {@link org.apache.commons.lang3.StringUtils#stripToNull}
     */
    @Contract("null -> null")
    public static String normalizeAndStripToNull(final String input) {
        return org.apache.commons.lang3.StringUtils.stripToNull(
                normalizeSpaceAndNonBreakingSpace(input));
    }
}
