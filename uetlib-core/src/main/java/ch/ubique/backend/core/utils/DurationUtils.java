package ch.ubique.backend.core.utils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public final class DurationUtils {

    private static final long SECOND_TO_NANOS = Duration.ofSeconds(1).toNanos();
    private static final long HOURS_TO_SECONDS = Duration.ofHours(1).toSeconds();

    private DurationUtils() {}

    public static Duration ofFractionalSeconds(final double seconds) {
        return Duration.ofNanos(Math.round(seconds * SECOND_TO_NANOS));
    }

    public static double toFractionalSeconds(final Duration duration) {
        return (double) duration.toNanos() / SECOND_TO_NANOS;
    }

    /**
     * This is distinct from {@link Duration#toHours()} which returns the hours as a floored {@link
     * long};
     */
    public static double toFractionalHours(final Duration duration) {
        return (double) duration.toSeconds() / HOURS_TO_SECONDS;
    }

    /**
     * Adapted from <a href="https://stackoverflow.com/a/37423588/15150382">here</a>. Truncates the
     * given input to the given field with the given increment.
     *
     * @param input to floor
     * @param floorTo field to floor to
     * @param roundIncrement floor to multiples of this value in the given field
     * @return a copy representing the floored value
     */
    public static Duration floorTo(
            final Duration input, final TemporalUnit floorTo, final int roundIncrement) {
        /* Extract the field being floored. */
        final var field = input.get(floorTo);

        /* Distance from previous floor. */
        final var remainder = field % roundIncrement;

        return floor(input, floorTo, remainder);
    }

    /**
     * Adapted from <a href="https://stackoverflow.com/a/37423588/15150382">here</a>. Ceils the
     * given input to the given field with the given increment.
     *
     * @param input to ceil
     * @param ceilTo field to ceil to
     * @param roundIncrement ceil to multiples of this value in the given field
     * @return a copy representing the ceiled value
     */
    public static Duration ceilTo(
            final Duration input, final TemporalUnit ceilTo, final int roundIncrement) {
        /* Extract the field being rounded. */
        final var field = input.get(ceilTo);

        /* Distance from previous floor. */
        final var remainder = field % roundIncrement;

        final var floor = floor(input, ceilTo, remainder);
        return input.equals(floor) ? floor : ceil(input, ceilTo, roundIncrement, remainder);
    }

    private static Duration floor(
            final Duration input, final TemporalUnit floorTo, final long remainder) {
        return input.plus(-remainder, floorTo).truncatedTo(floorTo);
    }

    /**
     * helper function for floorTo/ceilTo. This function ALWAYS ceils, even if input = floor(input)
     */
    private static Duration ceil(
            final Duration input,
            final TemporalUnit ceilTo,
            final int roundIncrement,
            final long remainder) {
        return input.plus(roundIncrement - remainder, ceilTo).truncatedTo(ceilTo);
    }

    public static Duration floorToQuarterHours(final Duration input) {
        return floorTo(input, ChronoUnit.SECONDS, 15 * 60);
    }

    public static Duration ceilToQuarterHours(final Duration input) {
        return ceilTo(input, ChronoUnit.SECONDS, 15 * 60);
    }

    public static Duration ceilToHours(final Duration input) {
        return ceilTo(input, ChronoUnit.SECONDS, 60 * 60);
    }

    public static Duration floorToHours(final Duration input) {
        return floorTo(input, ChronoUnit.SECONDS, 60 * 60);
    }

    public static Duration ceilToHours(final ZonedDateTime startAt, final ZonedDateTime endAt) {
        return ceilToHours(Duration.between(startAt, endAt));
    }

    public static Duration ceilToQuarterHours(
            final ZonedDateTime startAt, final ZonedDateTime endAt) {
        return ceilToQuarterHours(Duration.between(startAt, endAt));
    }
}
