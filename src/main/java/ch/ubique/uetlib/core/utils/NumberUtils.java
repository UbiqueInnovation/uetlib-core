package ch.ubique.uetlib.core.utils;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public final class NumberUtils {

    private NumberUtils() {}

    // requirePositive

    /**
     * @param value short
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static short requirePositive(final short value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value must be positive");
        }
        return value;
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static short requirePositive(
            final short value, final @Nullable Supplier<String> messageSupplier) {
        if (value <= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static short requirePositive(final short value, final @Nullable String message) {
        if (value <= 0) {
            final var resolvedMessage = message != null ? message : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static int requirePositive(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value must be positive");
        }
        return value;
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static int requirePositive(
            final int value, final @Nullable Supplier<String> messageSupplier) {
        if (value <= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static int requirePositive(final int value, final @Nullable String message) {
        if (value <= 0) {
            final var resolvedMessage = message != null ? message : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static long requirePositive(final long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value must be positive");
        }
        return value;
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static long requirePositive(
            final long value, final @Nullable Supplier<String> messageSupplier) {
        if (value <= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static long requirePositive(final long value, final @Nullable String message) {
        if (value <= 0) {
            final var resolvedMessage = message != null ? message : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static double requirePositive(final double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value must be positive");
        }
        return value;
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static double requirePositive(
            final double value, final @Nullable Supplier<String> messageSupplier) {
        if (value <= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static double requirePositive(final double value, final @Nullable String message) {
        if (value <= 0) {
            final var resolvedMessage = message != null ? message : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static float requirePositive(final float value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value must be positive");
        }
        return value;
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static float requirePositive(
            final float value, final @Nullable Supplier<String> messageSupplier) {
        if (value <= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static float requirePositive(final float value, final @Nullable String message) {
        if (value <= 0) {
            final var resolvedMessage = message != null ? message : "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is positive (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requirePositive(final T value) {
        requireNonNull(value, "value must be positive");
        if (value.doubleValue() <= 0) {
            final var resolvedMessage = "value must be positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requirePositive(
            final T value, final @Nullable Supplier<String> messageSupplier) {
        final var resolvedMessage =
                messageSupplier != null ? messageSupplier.get() : "value must be positive";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() <= 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requirePositive(
            final T value, final @Nullable String message) {
        final var resolvedMessage = message != null ? message : "value must be positive";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() <= 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    // requireNonNegative

    /**
     * @param value short
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static short requireNonNegative(final short value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative");
        }
        return value;
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static short requireNonNegative(
            final short value, final @Nullable Supplier<String> messageSupplier) {
        if (value < 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static short requireNonNegative(final short value, final @Nullable String message) {
        if (value < 0) {
            final var resolvedMessage = message != null ? message : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static int requireNonNegative(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative");
        }
        return value;
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static int requireNonNegative(
            final int value, final @Nullable Supplier<String> messageSupplier) {
        if (value < 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static int requireNonNegative(final int value, final @Nullable String message) {
        if (value < 0) {
            final var resolvedMessage = message != null ? message : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static long requireNonNegative(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative");
        }
        return value;
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static long requireNonNegative(
            final long value, final @Nullable Supplier<String> messageSupplier) {
        if (value < 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static long requireNonNegative(final long value, final @Nullable String message) {
        if (value < 0) {
            final var resolvedMessage = message != null ? message : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static double requireNonNegative(final double value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative");
        }
        return value;
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static double requireNonNegative(
            final double value, final @Nullable Supplier<String> messageSupplier) {
        if (value < 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static double requireNonNegative(final double value, final @Nullable String message) {
        if (value < 0) {
            final var resolvedMessage = message != null ? message : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static float requireNonNegative(final float value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative");
        }
        return value;
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static float requireNonNegative(
            final float value, final @Nullable Supplier<String> messageSupplier) {
        if (value < 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static float requireNonNegative(final float value, final @Nullable String message) {
        if (value < 0) {
            final var resolvedMessage = message != null ? message : "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNonNegative(final T value) {
        requireNonNull(value, "value must be non-negative");
        if (value.doubleValue() < 0) {
            final var resolvedMessage = "value must be non-negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonNegative(
            final T value, final @Nullable Supplier<String> messageSupplier) {
        final var resolvedMessage =
                messageSupplier != null ? messageSupplier.get() : "value must be non-negative";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonNegative(
            final T value, final @Nullable String message) {
        final var resolvedMessage = message != null ? message : "value must be non-negative";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    // requireNegative

    /**
     * @param value short
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static short requireNegative(final short value) {
        if (value >= 0) {
            throw new IllegalArgumentException("value must be negative");
        }
        return value;
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static short requireNegative(
            final short value, final @Nullable Supplier<String> messageSupplier) {
        if (value >= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static short requireNegative(final short value, final @Nullable String message) {
        if (value >= 0) {
            final var resolvedMessage = message != null ? message : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static int requireNegative(final int value) {
        if (value >= 0) {
            throw new IllegalArgumentException("value must be negative");
        }
        return value;
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static int requireNegative(
            final int value, final @Nullable Supplier<String> messageSupplier) {
        if (value >= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static int requireNegative(final int value, final @Nullable String message) {
        if (value >= 0) {
            final var resolvedMessage = message != null ? message : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static long requireNegative(final long value) {
        if (value >= 0) {
            throw new IllegalArgumentException("value must be negative");
        }
        return value;
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static long requireNegative(
            final long value, final @Nullable Supplier<String> messageSupplier) {
        if (value >= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static long requireNegative(final long value, final @Nullable String message) {
        if (value >= 0) {
            final var resolvedMessage = message != null ? message : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static double requireNegative(final double value) {
        if (value >= 0) {
            throw new IllegalArgumentException("value must be negative");
        }
        return value;
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static double requireNegative(
            final double value, final @Nullable Supplier<String> messageSupplier) {
        if (value >= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static double requireNegative(final double value, final @Nullable String message) {
        if (value >= 0) {
            final var resolvedMessage = message != null ? message : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static float requireNegative(final float value) {
        if (value >= 0) {
            throw new IllegalArgumentException("value must be negative");
        }
        return value;
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static float requireNegative(
            final float value, final @Nullable Supplier<String> messageSupplier) {
        if (value >= 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static float requireNegative(final float value, final @Nullable String message) {
        if (value >= 0) {
            final var resolvedMessage = message != null ? message : "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNegative(final T value) {
        requireNonNull(value, "value must be negative");
        if (value.doubleValue() >= 0) {
            final var resolvedMessage = "value must be negative";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNegative(
            final T value, final @Nullable Supplier<String> messageSupplier) {
        final var resolvedMessage =
                messageSupplier != null ? messageSupplier.get() : "value must be negative";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() >= 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNegative(
            final T value, final @Nullable String message) {
        final var resolvedMessage = message != null ? message : "value must be negative";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() >= 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    // requireNonPositive

    /**
     * @param value short
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static short requireNonPositive(final short value) {
        if (value > 0) {
            throw new IllegalArgumentException("value must be non-positive");
        }
        return value;
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static short requireNonPositive(
            final short value, final @Nullable Supplier<String> messageSupplier) {
        if (value > 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static short requireNonPositive(final short value, final @Nullable String message) {
        if (value > 0) {
            final var resolvedMessage = message != null ? message : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static int requireNonPositive(final int value) {
        if (value > 0) {
            throw new IllegalArgumentException("value must be non-positive");
        }
        return value;
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static int requireNonPositive(
            final int value, final @Nullable Supplier<String> messageSupplier) {
        if (value > 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static int requireNonPositive(final int value, final @Nullable String message) {
        if (value > 0) {
            final var resolvedMessage = message != null ? message : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static long requireNonPositive(final long value) {
        if (value > 0) {
            throw new IllegalArgumentException("value must be non-positive");
        }
        return value;
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static long requireNonPositive(
            final long value, final @Nullable Supplier<String> messageSupplier) {
        if (value > 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static long requireNonPositive(final long value, final @Nullable String message) {
        if (value > 0) {
            final var resolvedMessage = message != null ? message : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static double requireNonPositive(final double value) {
        if (value > 0) {
            throw new IllegalArgumentException("value must be non-positive");
        }
        return value;
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static double requireNonPositive(
            final double value, final @Nullable Supplier<String> messageSupplier) {
        if (value > 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static double requireNonPositive(final double value, final @Nullable String message) {
        if (value > 0) {
            final var resolvedMessage = message != null ? message : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static float requireNonPositive(final float value) {
        if (value > 0) {
            throw new IllegalArgumentException("value must be non-positive");
        }
        return value;
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static float requireNonPositive(
            final float value, final @Nullable Supplier<String> messageSupplier) {
        if (value > 0) {
            final var resolvedMessage =
                    messageSupplier != null ? messageSupplier.get() : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static float requireNonPositive(final float value, final @Nullable String message) {
        if (value > 0) {
            final var resolvedMessage = message != null ? message : "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNonPositive(final T value) {
        requireNonNull(value, "value must be non-positive");
        if (value.doubleValue() > 0) {
            final var resolvedMessage = "value must be non-positive";
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonPositive(
            final T value, final @Nullable Supplier<String> messageSupplier) {
        final var resolvedMessage =
                messageSupplier != null ? messageSupplier.get() : "value must be non-positive";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() > 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonPositive(
            final T value, final @Nullable String message) {
        final var resolvedMessage = message != null ? message : "value must be non-positive";
        requireNonNull(value, resolvedMessage);
        if (value.doubleValue() > 0) {
            throw new IllegalArgumentException(resolvedMessage);
        }
        return value;
    }
}
