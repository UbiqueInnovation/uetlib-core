package ch.ubique.backend.core.utils;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

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

    public static char singletonOrNull(final char[] array) {
        if (isEmpty(array)) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Can extract singleton only when array length == 1");
        }
        return array[0];
    }

    public static int singletonOrNull(final int[] array) {
        if (isEmpty(array)) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Can extract singleton only when array length == 1");
        }
        return array[0];
    }

    public static long singletonOrNull(final long[] array) {
        if (isEmpty(array)) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Can extract singleton only when array length == 1");
        }
        return array[0];
    }

    public static double singletonOrNull(final double[] array) {
        if (isEmpty(array)) {
            return 0;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Can extract singleton only when array length == 1");
        }
        return array[0];
    }

    public static <T> T singletonOrNull(final T[] array) {
        if (isEmpty(array)) {
            return null;
        }
        if (array.length > 1) {
            throw new IllegalArgumentException("Can extract singleton only when array length == 1");
        }
        return array[0];
    }
}
