package ch.ubique.backend.core.utils;

public final class MathUtils {

    private MathUtils() {}

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}
