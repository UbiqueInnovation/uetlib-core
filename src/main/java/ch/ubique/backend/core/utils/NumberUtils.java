package ch.ubique.backend.core.utils;

import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

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
        return requirePositive(value, "Value must be positive");
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static short requirePositive(final short value, final Supplier<String> messageSupplier) {
        return requirePositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static short requirePositive(final short value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
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
        return requirePositive(value, "Value must be positive");
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static int requirePositive(final int value, final Supplier<String> messageSupplier) {
        return requirePositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static int requirePositive(final int value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
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
        return requirePositive(value, "Value must be positive");
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static long requirePositive(final long value, final Supplier<String> messageSupplier) {
        return requirePositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static long requirePositive(final long value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
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
        return requirePositive(value, "Value must be positive");
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static double requirePositive(final double value, final Supplier<String> messageSupplier) {
        return requirePositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static double requirePositive(final double value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
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
        return requirePositive(value, "Value must be positive");
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static float requirePositive(final float value, final Supplier<String> messageSupplier) {
        return requirePositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is negative or zero
     */
    @Contract(pure = true)
    public static float requirePositive(final float value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative or zero
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requirePositive(final T value) {
        return requirePositive(value, "Value must be positive");
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requirePositive(final T value, final Supplier<String> messageSupplier) {
        return requirePositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requirePositive(final T value, final String message) {
        if (value == null || value.doubleValue() <= 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonNegative(value, "Value must be non-negative");
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static short requireNonNegative(final short value, final Supplier<String> messageSupplier) {
        return requireNonNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static short requireNonNegative(final short value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonNegative(value, "Value must be non-negative");
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static int requireNonNegative(final int value, final Supplier<String> messageSupplier) {
        return requireNonNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static int requireNonNegative(final int value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonNegative(value, "Value must be non-negative");
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static long requireNonNegative(final long value, final Supplier<String> messageSupplier) {
        return requireNonNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static long requireNonNegative(final long value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonNegative(value, "Value must be non-negative");
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static double requireNonNegative(final double value, final Supplier<String> messageSupplier) {
        return requireNonNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static double requireNonNegative(final double value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonNegative(value, "Value must be non-negative");
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static float requireNonNegative(final float value, final Supplier<String> messageSupplier) {
        return requireNonNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is negative
     */
    @Contract(pure = true)
    public static float requireNonNegative(final float value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNonNegative(final T value) {
        return requireNonNegative(value, "Value must be non-negative");
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonNegative(final T value, final Supplier<String> messageSupplier) {
        return requireNonNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonNegative(final T value, final String message) {
        if (value == null || value.doubleValue() < 0) {
            throw new IllegalArgumentException(message);
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
        return requireNegative(value, "Value must be negative");
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static short requireNegative(final short value, final Supplier<String> messageSupplier) {
        return requireNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static short requireNegative(final short value, final String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
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
        return requireNegative(value, "Value must be negative");
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static int requireNegative(final int value, final Supplier<String> messageSupplier) {
        return requireNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static int requireNegative(final int value, final String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
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
        return requireNegative(value, "Value must be negative");
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static long requireNegative(final long value, final Supplier<String> messageSupplier) {
        return requireNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static long requireNegative(final long value, final String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
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
        return requireNegative(value, "Value must be negative");
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static double requireNegative(final double value, final Supplier<String> messageSupplier) {
        return requireNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static double requireNegative(final double value, final String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
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
        return requireNegative(value, "Value must be negative");
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static float requireNegative(final float value, final Supplier<String> messageSupplier) {
        return requireNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive or zero
     */
    @Contract(pure = true)
    public static float requireNegative(final float value, final String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive or zero
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNegative(final T value) {
        return requireNegative(value, "Value must be negative");
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNegative(final T value, final Supplier<String> messageSupplier) {
        return requireNegative(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive or zero
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNegative(final T value, final String message) {
        if (value == null || value.doubleValue() >= 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonPositive(value, "Value must be non-positive");
    }

    /**
     * @param value short
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static short requireNonPositive(final short value, final Supplier<String> messageSupplier) {
        return requireNonPositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value short
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static short requireNonPositive(final short value, final String message) {
        if (value > 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonPositive(value, "Value must be non-positive");
    }

    /**
     * @param value int
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static int requireNonPositive(final int value, final Supplier<String> messageSupplier) {
        return requireNonPositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value int
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static int requireNonPositive(final int value, final String message) {
        if (value > 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonPositive(value, "Value must be non-positive");
    }

    /**
     * @param value long
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static long requireNonPositive(final long value, final Supplier<String> messageSupplier) {
        return requireNonPositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value long
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static long requireNonPositive(final long value, final String message) {
        if (value > 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonPositive(value, "Value must be non-positive");
    }

    /**
     * @param value double
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static double requireNonPositive(final double value, final Supplier<String> messageSupplier) {
        return requireNonPositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value double
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static double requireNonPositive(final double value, final String message) {
        if (value > 0) {
            throw new IllegalArgumentException(message);
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
        return requireNonPositive(value, "Value must be non-positive");
    }

    /**
     * @param value float
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static float requireNonPositive(final float value, final Supplier<String> messageSupplier) {
        return requireNonPositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value float
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is positive
     */
    @Contract(pure = true)
    public static float requireNonPositive(final float value, final String message) {
        if (value > 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * @param value number
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNonPositive(final T value) {
        return requireNonPositive(value, "Value must be non-positive");
    }

    /**
     * @param value number
     * @param messageSupplier custom error message supplier
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonPositive(final T value, final Supplier<String> messageSupplier) {
        return requireNonPositive(value, messageSupplier != null ? messageSupplier.get() : null);
    }

    /**
     * @param value number
     * @param message custom error message
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive
     */
    @Contract(value = "null, _ -> fail", pure = true)
    public static <T extends Number> T requireNonPositive(final T value, final String message) {
        if (value == null || value.doubleValue() > 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
