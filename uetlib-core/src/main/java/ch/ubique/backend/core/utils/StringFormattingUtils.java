package ch.ubique.backend.core.utils;

import java.time.Duration;
import java.util.ArrayList;

public class StringFormattingUtils {

    public static String millisToString(long durationInMillis) {
        durationInMillis = Math.abs(durationInMillis);
        if (durationInMillis == 0) {
            return "0";
        }

        long millis = durationInMillis % 1000;
        long seconds = (durationInMillis / Duration.ofSeconds(1).toMillis()) % 60;
        long minutes = (durationInMillis / Duration.ofMinutes(1).toMillis()) % 60;
        long hours = (durationInMillis / Duration.ofHours(1).toMillis()) % 24;

        final var parts = new ArrayList<String>();

        if (hours > 0) {
            parts.add(String.format("%02dh", hours));
        }
        if (hours > 0 || minutes > 0) {
            parts.add(String.format("%02dm", minutes));
        }
        if (hours > 0 || minutes > 0 || seconds > 0) {
            parts.add(String.format("%02ds", seconds));
        }

        parts.add(String.format("%03dms", millis));

        return String.join(" ", parts);
    }
}
