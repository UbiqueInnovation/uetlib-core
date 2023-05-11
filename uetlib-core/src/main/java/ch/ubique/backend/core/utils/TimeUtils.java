package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;

import java.time.*;
import java.time.temporal.*;

public class TimeUtils {
    public static final String ZONE_NAME_UTC = "UTC";
    public static final String ZONE_NAME_EU_BERLIN = "Europe/Berlin";
    public static final String ZONE_NAME_EU_ZURICH = "Europe/Zurich";

    public static final ZoneId ZONE_ID_UTC = ZoneId.of(ZONE_NAME_UTC);
    public static final ZoneId ZONE_ID_EU_BERLIN = ZoneId.of(ZONE_NAME_EU_BERLIN);
    public static final ZoneId ZONE_ID_EU_ZURICH = ZoneId.of(ZONE_NAME_EU_ZURICH);

    private TimeUtils() {}

    public static ZonedDateTime now(final ZoneId zone) {
        return ZonedDateTime.now(zone);
    }

    public static ZonedDateTime nowEuZh() {
        return now(ZONE_ID_EU_ZURICH);
    }

    public static ZonedDateTime ofMillisEuZh(final long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZONE_ID_EU_ZURICH);
    }

    public static ZonedDateTime ofLocalDateLocalTimeEuZh(
            final LocalDate localDate, final LocalTime localTime) {
        return ZonedDateTime.of(localDate, localTime, ZONE_ID_EU_ZURICH);
    }

    public static ZonedDateTime withZoneSameInstantEuZh(final ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZONE_ID_EU_ZURICH);
    }

    public static ZonedDateTime withZoneSameInstantUtc(final ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZONE_ID_UTC);
    }

    public static LocalDate toLocalDateEuZh(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstantEuZh(zonedDateTime).toLocalDate();
    }

    public static LocalTime toLocalTimeEuZh(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstantEuZh(zonedDateTime).toLocalTime();
    }

    public static LocalDateTime toLocalDateTimeEuZh(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstantEuZh(zonedDateTime).toLocalDateTime();
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
    public static ZonedDateTime floorTo(
            final ZonedDateTime input, final TemporalField floorTo, final int roundIncrement) {
        /* Extract the field being floored. */
        final var field = input.getLong(floorTo);

        /* Distance from previous floor. */
        final var remainder = field % roundIncrement;

        return floor(input, floorTo, remainder);
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
    public static ZonedDateTime ceilTo(
            final ZonedDateTime input, final TemporalField ceilTo, final int roundIncrement) {
        /* Extract the field being rounded. */
        final var field = input.getLong(ceilTo);

        /* Distance from previous floor. */
        final var remainder = field % roundIncrement;

        final var floor = floor(input, ceilTo, remainder);
        return input.equals(floor) ? floor : ceil(input, ceilTo, roundIncrement, remainder);
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

    private static ZonedDateTime floor(
            final ZonedDateTime input, final TemporalField floorTo, final long remainder) {
        return input.plus(-remainder, floorTo.getBaseUnit()).truncatedTo(floorTo.getBaseUnit());
    }

    private static Duration floor(
            final Duration input, final TemporalUnit floorTo, final long remainder) {
        return input.plus(-remainder, floorTo).truncatedTo(floorTo);
    }

    /**
     * helper function for floorTo/ceilTo. This function ALWAYS ceils, even if input = floor(input)
     */
    private static ZonedDateTime ceil(
            final ZonedDateTime input,
            final TemporalField ceilTo,
            final int roundIncrement,
            final long remainder) {
        return input.plus(roundIncrement - remainder, ceilTo.getBaseUnit())
                .truncatedTo(ceilTo.getBaseUnit());
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

    public static ZonedDateTime floorToQuarterHours(final ZonedDateTime input) {
        return floorTo(input, ChronoField.MINUTE_OF_HOUR, 15);
    }

    public static Duration floorToQuarterHours(final Duration input) {
        return floorTo(input, ChronoUnit.SECONDS, 15 * 60);
    }

    public static ZonedDateTime ceilToQuarterHours(final ZonedDateTime input) {
        return ceilTo(input, ChronoField.MINUTE_OF_HOUR, 15);
    }

    public static Duration ceilToQuarterHours(final Duration input) {
        return ceilTo(input, ChronoUnit.SECONDS, 15 * 60);
    }

    public static ZonedDateTime ceilToHours(final ZonedDateTime input) {
        return ceilTo(input, ChronoField.MINUTE_OF_HOUR, 60);
    }

    public static Duration ceilToHours(final Duration input) {
        return ceilTo(input, ChronoUnit.SECONDS, 60 * 60);
    }

    public static ZonedDateTime floorToHours(final ZonedDateTime input) {
        return floorTo(input, ChronoField.MINUTE_OF_HOUR, 60);
    }

    public static Duration floorToHours(final Duration input) {
        return floorTo(input, ChronoUnit.SECONDS, 60 * 60);
    }

    /**
     * This is distinct from {@link Duration#toHours()} which returns the hours as a floored {@link
     * long};
     */
    public static double durationToHoursDouble(final Duration duration) {
        return (double) duration.getSeconds() / 60 / 60;
    }

    /**
     * @return true iff time1 >= time2
     */
    public static boolean isAfterOrEqual(
            @NotNull final ZonedDateTime time1, @NotNull final ZonedDateTime time2) {
        return !time1.isBefore(time2);
    }

    /**
     * @return true iff time1 <= time2
     */
    public static boolean isBeforeOrEqual(
            @NotNull final ZonedDateTime time1, @NotNull final ZonedDateTime time2) {
        return !time1.isAfter(time2);
    }

    /**
     * @param time the time to check
     * @param start of the valid interval [start, end)
     * @param end of the valid interval [start, end)
     * @return true <=> start <= time < end <=> time \in [start, end)
     */
    public static boolean isBetween(
            @NotNull final ZonedDateTime time,
            @NotNull final ZonedDateTime start,
            @NotNull final ZonedDateTime end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    public static boolean isMidnightEuZh(final ZonedDateTime zonedDateTime) {
        return toLocalTimeEuZh(zonedDateTime).equals(LocalTime.MIDNIGHT);
    }

    public static ZonedDateTime startOfTodayEuZh() {
        return nowEuZh().truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime startOfDayEuZh(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstantEuZh(zonedDateTime).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime startOfMonth(final ZoneId zone) {
        return now(zone).with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime startOfNextMonth(final ZoneId zone) {
        return now(zone).with(TemporalAdjusters.firstDayOfNextMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime startOfMonthEuZh() {
        return nowEuZh().with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime startOfNextMonthEuZh() {
        return nowEuZh().with(TemporalAdjusters.firstDayOfNextMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    public static Duration ceilToHours(final ZonedDateTime startAt, final ZonedDateTime endAt) {
        return ceilToHours(Duration.between(startAt, endAt));
    }

    public static Duration ceilToQuarterHours(
            final ZonedDateTime startAt, final ZonedDateTime endAt) {
        return ceilToQuarterHours(Duration.between(startAt, endAt));
    }
}
