package ch.ubique.backend.core.utils;

import java.time.Duration;

public class DurationUtils {

    private DurationUtils() {}

    public static Duration ofSeconds(final double seconds) {
        return Duration.ofNanos(Math.round(seconds * Duration.ofSeconds(1).toNanos()));
    }
}
