package ch.ubique.uetlib.core.utils;

import ch.ubique.uetlib.core.utils.duration.DurationBasedTemporalUnit;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public final class DurationUtils {

    public static final DurationBasedTemporalUnit FIVE_MINUTES =
            DurationBasedTemporalUnit.ofMinutes(5);
    public static final DurationBasedTemporalUnit FIFTEEN_MINUTES =
            DurationBasedTemporalUnit.ofMinutes(15);
    public static final DurationBasedTemporalUnit QUARTER_HOUR = FIFTEEN_MINUTES;
    public static final DurationBasedTemporalUnit THIRTY_MINUTES =
            DurationBasedTemporalUnit.ofMinutes(30);
    public static final DurationBasedTemporalUnit HALF_HOUR = THIRTY_MINUTES;
    public static final DurationBasedTemporalUnit FORTY_FIVE_MINUTES =
            DurationBasedTemporalUnit.ofMinutes(45);
    public static final DurationBasedTemporalUnit THREE_QUARTER_HOUR = FORTY_FIVE_MINUTES;

    /** Hours per day. */
    static final int HOURS_PER_DAY = 24;

    /** Minutes per hour. */
    static final int MINUTES_PER_HOUR = 60;

    /** Minutes per day. */
    static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;

    /** Seconds per minute. */
    static final int SECONDS_PER_MINUTE = 60;

    /** Seconds per hour. */
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    /** Seconds per day. */
    static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;

    /** Milliseconds per second. */
    static final long MILLIS_PER_SECOND = 1000L;

    /** Milliseconds per day. */
    static final long MILLIS_PER_DAY = MILLIS_PER_SECOND * SECONDS_PER_DAY;

    /** Microseconds per second. */
    static final long MICROS_PER_SECOND = 1000_000L;

    /** Microseconds per day. */
    static final long MICROS_PER_DAY = MICROS_PER_SECOND * SECONDS_PER_DAY;

    /** Nanos per millisecond. */
    static final long NANOS_PER_MILLI = 1000_000L;

    /** Nanos per second. */
    static final long NANOS_PER_SECOND = 1000_000_000L;

    /** Nanos per minute. */
    static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * SECONDS_PER_MINUTE;

    /** Nanos per hour. */
    static final long NANOS_PER_HOUR = NANOS_PER_MINUTE * MINUTES_PER_HOUR;

    /** Nanos per day. */
    static final long NANOS_PER_DAY = NANOS_PER_HOUR * HOURS_PER_DAY;

    private static final long SECOND_TO_NANOS = Duration.ofSeconds(1).toNanos();
    private static final long HOURS_TO_MILLIS = Duration.ofHours(1).toMillis();

    private DurationUtils() {}

    /** Truncates to nanos */
    public static Duration ofFractionalSeconds(final double seconds) {
        return Duration.ofNanos((long) Math.floor(seconds * SECOND_TO_NANOS));
    }

    /** Truncates to nanos */
    public static double toFractionalSeconds(final Duration duration) {
        return (double) duration.toNanos() / SECOND_TO_NANOS;
    }

    /** Truncates to millis */
    public static Duration ofFractionalHours(final double hours) {
        return Duration.ofMillis((long) Math.floor(hours * HOURS_TO_MILLIS));
    }

    /** Truncates to millis */
    public static double toFractionalHours(final Duration duration) {
        return (double) duration.toMillis() / HOURS_TO_MILLIS;
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
        final var durationBasedUnit =
                DurationBasedTemporalUnit.of(Duration.of(roundIncrement, floorTo));
        return input.truncatedTo(durationBasedUnit);
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
        final var durationBasedUnit =
                DurationBasedTemporalUnit.of(Duration.of(roundIncrement, ceilTo));
        return ceilTo(input, durationBasedUnit);
    }

    /**
     * @param input the duration to ceil
     * @param unit the temporal unit to round to. The unit must divide into a standard day without
     *     remainder!
     */
    public static Duration ceilTo(final Duration input, final TemporalUnit unit) {
        final var floor = input.truncatedTo(unit);
        return input.equals(floor) ? floor : floor.plus(1, unit);
    }

    public static Duration floorToQuarterHours(final Duration input) {
        return input.truncatedTo(QUARTER_HOUR);
    }

    public static Duration ceilToQuarterHours(final Duration input) {
        return ceilTo(input, QUARTER_HOUR);
    }

    public static Duration ceilToHours(final Duration input) {
        return ceilTo(input, ChronoUnit.HOURS);
    }

    public static Duration floorToHours(final Duration input) {
        return input.truncatedTo(ChronoUnit.HOURS);
    }

    public static Duration ceilToHours(final ZonedDateTime startAt, final ZonedDateTime endAt) {
        return ceilToHours(Duration.between(startAt, endAt));
    }

    public static Duration ceilToQuarterHours(
            final ZonedDateTime startAt, final ZonedDateTime endAt) {
        return ceilToQuarterHours(Duration.between(startAt, endAt));
    }
}
