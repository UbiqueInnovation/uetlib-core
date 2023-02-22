package ch.ubique.backend.core.utils;

import java.time.Duration;

public final class DurationUtils {

    private DurationUtils() {}

    private static final long SECOND_TO_NANOS = Duration.ofSeconds(1).toNanos();

    public static Duration ofSeconds(final double seconds) {
        return Duration.ofNanos(Math.round(seconds * SECOND_TO_NANOS));
    }

    public static double toSeconds(final Duration duration) {
        return 1.0 * duration.toNanos() / SECOND_TO_NANOS;
    }
}
