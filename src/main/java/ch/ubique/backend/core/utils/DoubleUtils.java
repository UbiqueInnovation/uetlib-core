package ch.ubique.backend.core.utils;

public final class DoubleUtils {

    private DoubleUtils() {}

    public static Double nullIfNaN(final Double value) {
        if (null != value && value.isNaN()) {
            return null;
        }
        return value;
    }
}
