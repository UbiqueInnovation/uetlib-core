package ch.uetlib.core.utils;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StringUtils {

    public static final String NON_BLANK_VALUE_MESSAGE = "value must be non-blank";

    private StringUtils() {}

    /**
     * Returns a {@link Stream#empty()} if {@code string == null || size <= 0 || size >
     * string.length()}
     */
    public static Stream<String> substringWindows(final @Nullable String string, final int size) {
        if (string == null || size <= 0 || size > string.length()) {
            return Stream.empty();
        }

        return IntStream.range(0, string.length() - size + 1)
                .mapToObj(start -> string.substring(start, start + size));
    }

    @Contract("null -> fail")
    public static String requireNonBlank(final @Nullable String value) {
        requireNonNull(value, NON_BLANK_VALUE_MESSAGE);
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            throw new IllegalStateException(NON_BLANK_VALUE_MESSAGE);
        }
        return value;
    }

    @Contract("null, _ -> fail")
    public static String requireNonBlank(
            final @Nullable String value, final @Nullable String message) {
        final var resolvedMessage = message != null ? message : NON_BLANK_VALUE_MESSAGE;
        requireNonNull(value, resolvedMessage);
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    @Contract("null, _ -> fail")
    public static String requireNonBlank(
            final @Nullable String value,
            final @Nullable Supplier<@Nullable String> messageSupplier) {
        requireNonNull(value, () -> resolveMessage(messageSupplier));
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            final var resolvedMessage = resolveMessage(messageSupplier);
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    private static String resolveMessage(
            @Nullable final Supplier<@Nullable String> messageSupplier) {
        final var suppliedMessage = messageSupplier != null ? messageSupplier.get() : null;
        return requireNonBlankElse(suppliedMessage, NON_BLANK_VALUE_MESSAGE);
    }

    @Contract("null, null -> fail")
    public static String requireNonBlankElse(
            final @Nullable String value, final @Nullable String defaultValue) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
            return value;
        }

        return requireNonBlank(defaultValue, "defaultValue must be non-blank");
    }

    @Contract("null, null -> fail")
    public static String requireNonBlankElseGet(
            final @Nullable String value,
            final @Nullable Supplier<@Nullable String> defaultValueSupplier) {
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
     * spaces {@code (\u00A0)} by spaces.
     */
    @Contract("null -> null")
    public static @Nullable String normalizeSpaceAndNonBreakingSpace(final @Nullable String input) {
        return input == null ? null : normalizeNonNullSpaceAndNonBreakingSpace(input);
    }

    public static String normalizeNonNullSpaceAndNonBreakingSpace(final String input) {
        // \u00A0 is non-breaking space
        final var nonBreakingSpaceReplaced = input.replace('\u00A0', ' ');
        return org.apache.commons.lang3.StringUtils.normalizeSpace(nonBreakingSpaceReplaced);
    }

    /**
     * normalizes the String using {@link #normalizeSpaceAndNonBreakingSpace} and strip whitespace
     * using {@link org.apache.commons.lang3.StringUtils#stripToNull}
     */
    @Contract("null -> null")
    public static @Nullable String normalizeAndStripToNull(final @Nullable String input) {
        return org.apache.commons.lang3.StringUtils.stripToNull(
                normalizeSpaceAndNonBreakingSpace(input));
    }

    public static Optional<Integer> tryParseInt(final @Nullable String s) {
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
