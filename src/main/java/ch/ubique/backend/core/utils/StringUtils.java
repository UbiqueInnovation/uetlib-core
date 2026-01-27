package ch.ubique.backend.core.utils;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
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
    public static String requireNonBlank(final String value) {
        requireNonNull(value, "value must be non-blank");
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            throw new IllegalStateException("value must be non-blank");
        }
        return value;
    }

    @Contract("null, _ -> fail")
    public static String requireNonBlank(final String value, @Nullable final String message) {
        final var resolvedMessage = message != null ? message : "value must be non-blank";
        requireNonNull(value, resolvedMessage);
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    @Contract("null, _ -> fail")
    public static String requireNonBlank(
            final String value, @Nullable final Supplier<String> messageSupplier) {
        requireNonNull(value, "value must be non-blank");
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-blank";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    @Contract("null, _ -> fail; _, null -> fail")
    public static String requireNonBlankElse(final String value, final String defaultValue) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
            return value;
        }

        return requireNonBlank(defaultValue, "defaultValue must be non-blank");
    }

    @Contract("null, _ -> fail; _, null -> fail")
    public static String requireNonBlankElseGet(
            final String value, final Supplier<String> defaultValueSupplier) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
            return value;
        }

        requireNonNull(defaultValueSupplier, "defaultValueSupplier must not be null");

        final var defaultValue = defaultValueSupplier.get();
        return requireNonBlank(defaultValue, "defaultValue must be non-blank");
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

    public static Optional<Integer> tryParseInt(final String s) {
        if (NumberUtils.isParsable(s)) {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (final NumberFormatException ignored) {
                return Optional.of(Double.parseDouble(s)).map(Double::intValue);
            }
        }
        return Optional.empty();
    }
}
