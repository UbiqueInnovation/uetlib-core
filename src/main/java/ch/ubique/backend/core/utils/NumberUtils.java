package ch.ubique.backend.core.utils;

import org.jetbrains.annotations.Contract;

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
            throw new IllegalArgumentException("Value must be positive");
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
            throw new IllegalArgumentException("Value must be positive");
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
            throw new IllegalArgumentException("Value must be positive");
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
            throw new IllegalArgumentException("Value must be positive");
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
            throw new IllegalArgumentException("Value must be positive");
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is positive (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative or zero
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requirePositive(final T value) {
        if (value == null || value.doubleValue() <= 0) {
            throw new IllegalArgumentException("Value must be positive");
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
            throw new IllegalArgumentException("Value must be non-negative");
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
            throw new IllegalArgumentException("Value must be non-negative");
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
            throw new IllegalArgumentException("Value must be non-negative");
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
            throw new IllegalArgumentException("Value must be non-negative");
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
            throw new IllegalArgumentException("Value must be non-negative");
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or negative
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNonNegative(final T value) {
        if (value == null || value.doubleValue() < 0) {
            throw new IllegalArgumentException("Value must be non-negative");
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
            throw new IllegalArgumentException("Value must be negative");
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
            throw new IllegalArgumentException("Value must be negative");
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
            throw new IllegalArgumentException("Value must be negative");
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
            throw new IllegalArgumentException("Value must be negative");
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
            throw new IllegalArgumentException("Value must be negative");
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive or zero
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNegative(final T value) {
        if (value == null || value.doubleValue() >= 0) {
            throw new IllegalArgumentException("Value must be negative");
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
            throw new IllegalArgumentException("Value must be non-positive");
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
            throw new IllegalArgumentException("Value must be non-positive");
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
            throw new IllegalArgumentException("Value must be non-positive");
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
            throw new IllegalArgumentException("Value must be non-positive");
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
            throw new IllegalArgumentException("Value must be non-positive");
        }
        return value;
    }

    /**
     * @param value float
     * @return the value, if it is negative (excluding zero)
     * @throws IllegalArgumentException if the value is null or positive
     */
    @Contract(value = "null -> fail", pure = true)
    public static <T extends Number> T requireNonPositive(final T value) {
        if (value == null || value.doubleValue() > 0) {
            throw new IllegalArgumentException("Value must be non-positive");
        }
        return value;
    }
}
