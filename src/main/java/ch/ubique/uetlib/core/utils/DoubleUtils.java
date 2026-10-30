package ch.ubique.uetlib.core.utils;

import org.jspecify.annotations.Nullable;

public final class DoubleUtils {

    private DoubleUtils() {}

    @Nullable
    public static Double nullIfNaN(final @Nullable Double value) {
        if (null != value && value.isNaN()) {
            return null;
        }
        return value;
    }
}
