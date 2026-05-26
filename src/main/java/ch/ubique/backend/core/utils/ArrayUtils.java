package ch.ubique.backend.core.utils;

import org.jspecify.annotations.Nullable;

@SuppressWarnings("java:S1192")
public final class ArrayUtils {

    private ArrayUtils() {}

    public static char lastElement(final char[] array) {
        return array[array.length - 1];
    }

    public static int lastElement(final int[] array) {
        return array[array.length - 1];
    }

    public static long lastElement(final long[] array) {
        return array[array.length - 1];
    }

    public static double lastElement(final double[] array) {
        return array[array.length - 1];
    }

    public static <T> T lastElement(final T[] array) {
        return array[array.length - 1];
    }

    public static char singletonOrZero(final char @Nullable [] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Cannot extract singleton when array length > 1");
        }
        return array[0];
    }

    public static int singletonOrZero(final int @Nullable [] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Cannot extract singleton when array length > 1");
        }
        return array[0];
    }

    public static long singletonOrZero(final long @Nullable [] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Cannot extract singleton when array length > 1");
        }
        return array[0];
    }

    public static double singletonOrZero(final double @Nullable [] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Cannot extract singleton when array length > 1");
        }
        return array[0];
    }

    @Nullable
    public static <T extends @Nullable Object> T singletonOrNull(
            final @Nullable T @Nullable [] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Cannot extract singleton when array length > 1");
        }
        return array[0];
    }
}
