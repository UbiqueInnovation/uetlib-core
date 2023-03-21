package ch.ubique.backend.core.utils;

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
}
