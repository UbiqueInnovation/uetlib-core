package ch.ubique.backend.core.utils;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Contract;

import java.time.*;
import java.time.temporal.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Access instances of {@link TimeUtils} via the provided static fields {@link
 * TimeUtils#getInstance} to retrieve an instance for the given {@link ZoneId}. The supplied {@link
 * ZoneId} is normalized, see {@link ZoneId#normalized()}.
 */
public final class TimeUtils {

    public static final String ZONE_NAME_GMT = "GMT";
    public static final String ZONE_NAME_UTC = "UTC";
    public static final String ZONE_NAME_EUROPE_BERLIN = "Europe/Berlin";
    public static final String ZONE_NAME_EUROPE_ZURICH = "Europe/Zurich";

    public static final ZoneId ZONE_ID_GMT = ZoneId.of(ZONE_NAME_GMT).normalized();
    public static final TimeUtils TIME_UTILS_GMT = new TimeUtils(ZONE_ID_GMT);

    public static final ZoneId ZONE_ID_UTC = ZoneId.of(ZONE_NAME_UTC).normalized();
    public static final TimeUtils TIME_UTILS_UTC = new TimeUtils(ZONE_ID_UTC);

    public static final ZoneId ZONE_ID_EUROPE_ZURICH =
            ZoneId.of(ZONE_NAME_EUROPE_ZURICH).normalized();
    public static final TimeUtils TIME_UTILS_EUROPE_ZURICH = new TimeUtils(ZONE_ID_EUROPE_ZURICH);

    public static final ZoneId ZONE_ID_EUROPE_BERLIN =
            ZoneId.of(ZONE_NAME_EUROPE_BERLIN).normalized();
    public static final TimeUtils TIME_UTILS_EUROPE_BERLIN = new TimeUtils(ZONE_ID_EUROPE_BERLIN);

    private static final Map<ZoneId, TimeUtils> INSTANCES =
            new HashMap<>(
                    ofEntries(
                            entry(ZONE_ID_UTC, TIME_UTILS_UTC),
                            entry(ZONE_ID_EUROPE_ZURICH, TIME_UTILS_EUROPE_ZURICH),
                            entry(ZONE_ID_EUROPE_BERLIN, TIME_UTILS_EUROPE_BERLIN)));

    private final ZoneId zoneId;

    @Contract(pure = true)
    private TimeUtils(final ZoneId zoneId) {
        this.zoneId = zoneId.normalized();
    }

    public static TimeUtils getInstance(final ZoneId zoneId) {
        final var normalized = zoneId.normalized();
        return INSTANCES.computeIfAbsent(normalized, TimeUtils::new);
    }

    public static TimeUtils getInstance(final String zoneId) {
        return getInstance(ZoneId.of(zoneId));
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
    private static ZonedDateTime floor(
            final ZonedDateTime input, final TemporalField floorTo, final long remainder) {
        return input.plus(-remainder, floorTo.getBaseUnit()).truncatedTo(floorTo.getBaseUnit());
    }

    /**
     * helper function for floorTo/ceilTo. This function ALWAYS ceils, even if input = floor(input)
     */
    @Contract("_, _, _, _ -> new")
    private static ZonedDateTime ceil(
            final ZonedDateTime input,
            final TemporalField ceilTo,
            final int roundIncrement,
            final long remainder) {
        return input.plus(roundIncrement - remainder, ceilTo.getBaseUnit())
                .truncatedTo(ceilTo.getBaseUnit());
    }

    public static ZonedDateTime floorToQuarterHours(final ZonedDateTime input) {
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
     * @return {@code true iff temporal1 >= temporal2}
     */
    public static boolean isAfterOrEqual(final Instant temporal1, final Instant temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return {@code true iff temporal1 <= temporal2}
     */
    public static boolean isBeforeOrEqual(final Instant temporal1, final Instant temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return {@code true iff temporal1 >= temporal2}
     */
    public static boolean isAfterOrEqual(
            final ZonedDateTime temporal1, final ZonedDateTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return {@code true iff temporal1 <= temporal2}
     */
    public static boolean isBeforeOrEqual(
            final ZonedDateTime temporal1, final ZonedDateTime temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return {@code true iff temporal1 >= temporal2}
     */
    public static boolean isAfterOrEqual(
            final OffsetDateTime temporal1, final OffsetDateTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return {@code true iff temporal1 <= temporal2}
     */
    public static boolean isBeforeOrEqual(
            final OffsetDateTime temporal1, final OffsetDateTime temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return {@code true iff temporal1 >= temporal2}
     */
    public static boolean isAfterOrEqual(
            final LocalDateTime temporal1, final LocalDateTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return {@code true iff temporal1 <= temporal2}
     */
    public static boolean isBeforeOrEqual(
            final LocalDateTime temporal1, final LocalDateTime temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return {@code true iff temporal1 >= temporal2}
     */
    public static boolean isAfterOrEqual(final LocalDate temporal1, final LocalDate temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return {@code true iff temporal1 <= temporal2}
     */
    public static boolean isBeforeOrEqual(final LocalDate temporal1, final LocalDate temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @return {@code true iff temporal1 >= temporal2}
     */
    public static boolean isAfterOrEqual(final LocalTime temporal1, final LocalTime temporal2) {
        return !temporal1.isBefore(temporal2);
    }

    /**
     * @return {@code true iff temporal1 <= temporal2}
     */
    public static boolean isBeforeOrEqual(final LocalTime temporal1, final LocalTime temporal2) {
        return !temporal1.isAfter(temporal2);
    }

    /**
     * @param time the time to check
     * @param start of the valid interval [start, end)
     * @param end of the valid interval [start, end)
     * @return {@code true <=> start <= time < end <=> time \in [start, end)}
     */
    public static boolean isBetween(final Instant time, final Instant start, final Instant end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    /**
     * @param time the time to check
     * @param start of the valid interval [start, end)
     * @param end of the valid interval [start, end)
     * @return {@code true <=> start <= time < end <=> time \in [start, end)}
     */
    public static boolean isBetween(
            final ZonedDateTime time, final ZonedDateTime start, final ZonedDateTime end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    /**
     * @param time the time to check
     * @param start of the valid interval [start, end)
     * @param end of the valid interval [start, end)
     * @return {@code true <=> start <= time < end <=> time \in [start, end)}
     */
    public static boolean isBetween(
            final OffsetDateTime time, final OffsetDateTime start, final OffsetDateTime end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    /**
     * @param time the time to check
     * @param start of the valid interval [start, end)
     * @param end of the valid interval [start, end)
     * @return {@code true <=> start <= time < end <=> time \in [start, end)}
     */
    public static boolean isBetween(
            final LocalDateTime time, final LocalDateTime start, final LocalDateTime end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    /**
     * @param time the time to check
     * @param start of the valid interval [start, end)
     * @param end of the valid interval [start, end)
     * @return {@code true <=> start <= time < end <=> time \in [start, end)}
     */
    public static boolean isBetween(
            final LocalDate time, final LocalDate start, final LocalDate end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    /**
     * @param time the time to check
     * @param start of the valid interval [start, end)
     * @param end of the valid interval [start, end)
     * @return {@code true <=> start <= time < end <=> time \in [start, end)}
     */
    public static boolean isBetween(
            final LocalTime time, final LocalTime start, final LocalTime end) {
        return !time.isBefore(start) && time.isBefore(end);
    }

    public static long toMillis(final ZonedDateTime zonedDateTime) {
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

    @Contract(pure = true)
    public ZoneId zoneId() {
        return zoneId;
    }

    @Contract(" -> new")
    public ZonedDateTime now() {
        return ZonedDateTime.now(zoneId);
    }

    @Contract("_ -> new")
    public ZonedDateTime ofMillis(final long millis) {
        return Instant.ofEpochMilli(millis).atZone(zoneId);
    }

    @Contract("_ -> new")
    public ZonedDateTime ofInstant(final Instant instant) {
        return instant.atZone(zoneId);
    }

    @Contract("_, _ -> new")
    public ZonedDateTime ofLocalDateLocalTime(
            final LocalDate localDate, final LocalTime localTime) {
        return ZonedDateTime.of(localDate, localTime, zoneId);
    }

    @Contract("_ -> new")
    public ZonedDateTime ofLocalDateTime(final LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, zoneId);
    }

    @Contract("_ -> new")
    public ZonedDateTime withZoneSameInstant(final ZonedDateTime zonedDateTime) {
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

    @Contract("_ -> new")
    public ZonedDateTime atZoneSameInstant(final OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(zoneId);
    }

    public LocalDate toLocalDate(final OffsetDateTime offsetDateTime) {
        return atZoneSameInstant(offsetDateTime).toLocalDate();
    }

    public LocalTime toLocalTime(final OffsetDateTime offsetDateTime) {
        return atZoneSameInstant(offsetDateTime).toLocalTime();
    }

    public LocalDateTime toLocalDateTime(final OffsetDateTime offsetDateTime) {
        return atZoneSameInstant(offsetDateTime).toLocalDateTime();
    }

    @Contract("_ -> new")
    public ZonedDateTime atZone(final Instant instant) {
        return instant.atZone(zoneId);
    }

    public LocalDate toLocalDate(final Instant instant) {
        return atZone(instant).toLocalDate();
    }

    public LocalTime toLocalTime(final Instant instant) {
        return atZone(instant).toLocalTime();
    }

    public LocalDateTime toLocalDateTime(final Instant instant) {
        return atZone(instant).toLocalDateTime();
    }

    public boolean isMidnight(final ZonedDateTime zonedDateTime) {
        return toLocalTime(zonedDateTime).equals(LocalTime.MIDNIGHT);
    }

    @Contract(" -> new")
    public LocalDate today() {
        return LocalDate.now(zoneId);
    }

    @Contract(" -> new")
    public ZonedDateTime startOfToday() {
        return now().truncatedTo(ChronoUnit.DAYS);
    }

    @Contract("_ -> new")
    public ZonedDateTime startOfDay(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstant(zonedDateTime).truncatedTo(ChronoUnit.DAYS);
    }

    @Contract("_ -> new")
    public ZonedDateTime startOfDay(final LocalDate localDate) {
        return localDate.atStartOfDay(zoneId);
    }

    @Contract(" -> new")
    public ZonedDateTime startOfMonth() {
        return now().with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    @Contract("_ -> new")
    public ZonedDateTime startOfMonth(final ZonedDateTime zonedDateTime) {
        return withZoneSameInstant(zonedDateTime)
                .with(TemporalAdjusters.firstDayOfMonth())
                .truncatedTo(ChronoUnit.DAYS);
    }

    @Contract("_ -> new")
    public ZonedDateTime startOfMonth(final LocalDate localDate) {
        return startOfDay(localDate.with(TemporalAdjusters.firstDayOfMonth()));
    }

    @Contract(" -> new")
    public ZonedDateTime startOfNextMonth() {
        return now().with(TemporalAdjusters.firstDayOfNextMonth()).truncatedTo(ChronoUnit.DAYS);
    }
}
