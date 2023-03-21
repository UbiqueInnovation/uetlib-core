package ch.ubique.backend.core.utils;

public final class ArrayUtils {
    private ArrayUtils() {}

    public static <T> T arrayGetLast(final T[] array) {
        return array[array.length - 1];
    }
}
