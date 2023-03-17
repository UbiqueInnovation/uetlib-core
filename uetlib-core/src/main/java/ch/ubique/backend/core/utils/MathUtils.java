package ch.ubique.backend.core.utils;

public final class MathUtils {

    private MathUtils() {}

    public static int clamp(final int value, final int min, final int max) {
        return Math.max(min, Math.min(value, max));
    }
}
