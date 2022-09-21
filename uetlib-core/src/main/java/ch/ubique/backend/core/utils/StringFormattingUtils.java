package ch.ubique.backend.core.utils;

import java.time.Duration;
import java.util.ArrayList;

public class StringFormattingUtils {

    public static String millisToString(long durationInMillis) {
        durationInMillis = Math.abs(durationInMillis);
        if (durationInMillis == 0) {
            return "0";
        }

        final var seconds =
                Math.round(1f * durationInMillis / Duration.ofSeconds(1).toMillis()) % 60;
        final var minutes = (durationInMillis / Duration.ofMinutes(1).toMillis()) % 60;
        final var hours = (durationInMillis / Duration.ofHours(1).toMillis()) % 24;

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
