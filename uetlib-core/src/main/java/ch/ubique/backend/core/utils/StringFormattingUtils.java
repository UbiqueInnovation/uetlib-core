package ch.ubique.backend.core.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public final class StringFormattingUtils {

    private StringFormattingUtils() {}

    public static String formatDuration(final Instant from, final Instant to) {
        return millisToString(
                Duration.between(from, Objects.requireNonNullElse(to, Instant.now())));
    }

    public static String millisToString(final Duration duration) {
        return formatDuration(duration);
    }

    public static String formatDuration(final Duration duration) {
        if (Duration.ZERO.equals(duration)) {
            return "0s";
        }

        final var seconds = duration.toSecondsPart();
        final var minutes = duration.toMinutesPart();
        final var hours = duration.toHours();

        final var parts = new ArrayList<String>();

        if (hours > 0) {
            parts.add(String.format("%dh", hours));
        }
        if (hours > 0 || minutes > 0) {
            parts.add(String.format("%dm", minutes));
        }
        if (hours > 0 || minutes > 0 || seconds > 0) {
            parts.add(String.format("%ds", seconds));
        } else {
            parts.add("< 1s");
        }

        return String.join(" ", parts);
    }
}
