package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Contract;

import java.time.*;
import java.time.temporal.*;

public final class TimeUtils {

    public static final String ZONE_NAME_UTC = "UTC";
    public static final String ZONE_NAME_EUROPE_BERLIN = "Europe/Berlin";
    public static final String ZONE_NAME_EUROPE_ZURICH = "Europe/Zurich";

    public static final ZoneId ZONE_ID_UTC = ZoneId.of(ZONE_NAME_UTC);
    public static final TimeUtils TIME_UTILS_UTC = new TimeUtils(ZONE_ID_UTC);
    public static final ZoneId ZONE_ID_EUROPE_BERLIN = ZoneId.of(ZONE_NAME_EUROPE_BERLIN);
    public static final TimeUtils TIME_UTILS_EUROPE_BERLIN = new TimeUtils(ZONE_ID_EUROPE_BERLIN);
    public static final ZoneId ZONE_ID_EUROPE_ZURICH = ZoneId.of(ZONE_NAME_EUROPE_ZURICH);
    public static final TimeUtils TIME_UTILS_EUROPE_ZURICH = new TimeUtils(ZONE_ID_EUROPE_ZURICH);
    private final ZoneId zoneId;

    @Contract(pure = true)
    public TimeUtils(@NotNull final ZoneId zoneId) {
        this.zoneId = zoneId;
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
    public static @NotNull ZonedDateTime floorTo(
            final @NotNull ZonedDateTime input,
            final TemporalField floorTo,
            final int roundIncrement) {
        /* Extract the field being floored. */
        final var field = input.getLong(floorTo);

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

    @Contract("_, _, _ -> new")
    private static @NotNull ZonedDateTime floor(
            final @NotNull ZonedDateTime input,
            final @NotNull TemporalField floorTo,
            final long remainder) {
        return input.plus(-remainder, floorTo.getBaseUnit()).truncatedTo(floorTo.getBaseUnit());
    }

    /**
     * helper function for floorTo/ceilTo. This function ALWAYS ceils, even if input = floor(input)
     */
    @Contract("_, _, _, _ -> new")
    private static @NotNull ZonedDateTime ceil(
            final ZonedDateTime input,
            final @NotNull TemporalField ceilTo,
            final int roundIncrement,
            final long remainder) {
        return input.plus(roundIncrement - remainder, ceilTo.getBaseUnit())
                .truncatedTo(ceilTo.getBaseUnit());
    }

    public static @NotNull ZonedDateTime floorToQuarterHours(final ZonedDateTime input) {
        return floorTo(input, ChronoField.MINUTE_OF_HOUR, 15);
    }

    public static ZonedDateTime ceilToQuarterHours(final ZonedDateTime input) {
        return ceilTo(input, ChronoField.MINUTE_OF_HOUR, 15);
    }

    public static ZonedDateTime ceilToHours(final ZonedDateTime input) {
        return ceilTo(input, ChronoField.MINUTE_OF_HOUR, 60);
    }

    public static ZonedDateTime floorToHours(final ZonedDateTime input) {
        return floorTo(input, ChronoField.MINUTE_OF_HOUR, 60);
    }

    /**
     * @return true iff temporal1 >= temporal2
     */
    public static boolean isAfterOrEqual(
            @NotNull final Instant temporal1, @NotNull final Instant temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return true iff temporal1 <= temporal2
     */
    public static boolean isBeforeOrEqual(
            @NotNull final Instant temporal1, @NotNull final Instant temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return true iff temporal1 >= temporal2
     */
    public static boolean isAfterOrEqual(
            @NotNull final ZonedDateTime temporal1, @NotNull final ZonedDateTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return true iff temporal1 <= temporal2
     */
    public static boolean isBeforeOrEqual(
            @NotNull final ZonedDateTime temporal1, @NotNull final ZonedDateTime temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return true iff temporal1 >= temporal2
     */
    public static boolean isAfterOrEqual(
            @NotNull final OffsetDateTime temporal1, @NotNull final OffsetDateTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return true iff temporal1 <= temporal2
     */
    public static boolean isBeforeOrEqual(
            @NotNull final OffsetDateTime temporal1, @NotNull final OffsetDateTime temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return true iff temporal1 >= temporal2
     */
    public static boolean isAfterOrEqual(
            @NotNull final LocalDateTime temporal1, @NotNull final LocalDateTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return true iff temporal1 <= temporal2
     */
    public static boolean isBeforeOrEqual(
            @NotNull final LocalDateTime temporal1, @NotNull final LocalDateTime temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return true iff temporal1 >= temporal2
     */
    public static boolean isAfterOrEqual(
            @NotNull final LocalDate temporal1, @NotNull final LocalDate temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return true iff temporal1 <= temporal2
     */
    public static boolean isBeforeOrEqual(
            @NotNull final LocalDate temporal1, @NotNull final LocalDate temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return true iff temporal1 >= temporal2
     */
    public static boolean isAfterOrEqual(
            @NotNull final LocalTime temporal1, @NotNull final LocalTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return true iff temporal1 <= temporal2
     */
    public static boolean isBeforeOrEqual(
            @NotNull final LocalTime temporal1, @NotNull final LocalTime temporal2) {
        return !temporal1.isAfter(temporal2);
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

    public static long toMillis(final @NotNull ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    @SafeVarargs
    @Contract(pure = true)
    public static <T extends Comparable<? super T>> T min(final T... values) {
        return ObjectUtils.min(values);
    }

    @SafeVarargs
    @Contract(pure = true)
    public static <T extends Comparable<? super T>> T max(final T... values) {
        return ObjectUtils.max(values);
    }

    @Contract(" -> new")
    public @NotNull ZonedDateTime now() {
        return ZonedDateTime.now(zoneId);
    }

    @Contract("_ -> new")
    public @NotNull ZonedDateTime ofMillis(final long millis) {
        return Instant.ofEpochMilli(millis).atZone(zoneId);
    }

    @Contract("_, _ -> new")
    public @NotNull ZonedDateTime ofLocalDateLocalTime(
            final LocalDate localDate, final LocalTime localTime) {
        return ZonedDateTime.of(localDate, localTime, zoneId);
    }

    public @NotNull ZonedDateTime withZoneSameInstant(final @NotNull ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(zoneId);
    }

    public LocalDate toLocalDate(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstant(zonedDateTime).toLocalDate();
    }

    public LocalTime toLocalTime(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstant(zonedDateTime).toLocalTime();
    }

    public LocalDateTime toLocalDateTime(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstant(zonedDateTime).toLocalDateTime();
    }

    public boolean isMidnight(final ZonedDateTime zonedDateTime) {
        return toLocalTime(zonedDateTime).equals(LocalTime.MIDNIGHT);
    }

    @Contract(" -> new")
    public @NotNull ZonedDateTime startOfToday() {
        return now().truncatedTo(ChronoUnit.DAYS);
    }

    @Contract("_ -> new")
    public @NotNull ZonedDateTime startOfDay(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstant(zonedDateTime).truncatedTo(ChronoUnit.DAYS);
    }

    @Contract(" -> new")
    public @NotNull ZonedDateTime startOfMonth() {
        return now().with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    @Contract(" -> new")
    public @NotNull ZonedDateTime startOfNextMonth() {
        return now().with(TemporalAdjusters.firstDayOfNextMonth()).truncatedTo(ChronoUnit.DAYS);
    }
}
