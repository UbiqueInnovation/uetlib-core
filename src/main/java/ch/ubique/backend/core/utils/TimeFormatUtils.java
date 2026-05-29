package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.TimeUtils.*;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

import org.jetbrains.annotations.Contract;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class TimeFormatUtils {

    public static final TimeFormatUtils TIME_FORMAT_UTILS_UTC = new TimeFormatUtils(TIME_UTILS_UTC);
    public static final TimeFormatUtils TIME_FORMAT_UTILS_EUROPE_ZURICH =
            new TimeFormatUtils(TIME_UTILS_EUROPE_ZURICH);
    public static final TimeFormatUtils TIME_FORMAT_UTILS_EUROPE_BERLIN =
            new TimeFormatUtils(TIME_UTILS_EUROPE_BERLIN);

    private static final Map<ZoneId, TimeFormatUtils> INSTANCES =
            new HashMap<>(
                    ofEntries(
                            entry(ZONE_ID_UTC, TIME_FORMAT_UTILS_UTC),
                            entry(ZONE_ID_EUROPE_ZURICH, TIME_FORMAT_UTILS_EUROPE_ZURICH),
                            entry(ZONE_ID_EUROPE_BERLIN, TIME_FORMAT_UTILS_EUROPE_BERLIN)));

    private static final DateTimeFormatter PRETTY_LOCAL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter PRETTY_LOCAL_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter PRETTY_LOCAL_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final TimeUtils timeUtils;

    @Contract(pure = true)
    private TimeFormatUtils(final TimeUtils timeUtils) {
        this.timeUtils = timeUtils;
    }

    public static TimeFormatUtils getInstance(final ZoneId zoneId) {
        final var normalized = zoneId.normalized();
        return INSTANCES.computeIfAbsent(
                normalized, zi -> new TimeFormatUtils(TimeUtils.getInstance(zi)));
    }

    public static TimeFormatUtils getInstance(final String zoneId) {
        return getInstance(ZoneId.of(zoneId));
    }

    public static LocalDate parsePrettyLocalDate(final String localDate) {
        return LocalDate.parse(localDate, PRETTY_LOCAL_DATE_FORMATTER);
    }

    public static LocalTime parsePrettyLocalTime(final String localTime) {
        return LocalTime.parse(localTime, PRETTY_LOCAL_TIME_FORMATTER);
    }

    public static LocalDateTime parsePrettyLocalDateTime(final String localDateTime) {
        return LocalDateTime.parse(localDateTime, PRETTY_LOCAL_DATE_TIME_FORMATTER);
    }

    public ZonedDateTime parsePrettyLocalDateLocalTimeAtZone(
            final String localDate, final String localTime) {
        return timeUtils.ofLocalDateLocalTime(
                parsePrettyLocalDate(localDate), parsePrettyLocalTime(localTime));
    }

    public ZonedDateTime parsePrettyLocalDateTimeAtZone(final String localDateTime) {
        return timeUtils.ofLocalDateTime(parsePrettyLocalDateTime(localDateTime));
    }

    public String formatLocalDate(final ZonedDateTime temporal) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(temporal));
    }

    public String formatLocalDate(final OffsetDateTime temporal) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(temporal));
    }

    public String formatLocalDate(final Instant temporal) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(temporal));
    }

    public String formatLocalDate(final LocalDateTime temporal) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(temporal);
    }

    public String formatLocalDate(final LocalDate temporal) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(temporal);
    }

    public String formatLocalTime(final ZonedDateTime temporal) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(temporal));
    }

    public String formatLocalTime(final OffsetDateTime temporal) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(temporal));
    }

    public String formatLocalTime(final Instant temporal) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(temporal));
    }

    public String formatLocalTime(final LocalDateTime temporal) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(temporal);
    }

    public String formatLocalTime(final LocalTime temporal) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(temporal);
    }

    public String formatLocalDateTime(final ZonedDateTime temporal) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(temporal));
    }

    public String formatLocalDateTime(final OffsetDateTime temporal) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(temporal));
    }

    public String formatLocalDateTime(final Instant temporal) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(temporal));
    }

    public String formatLocalDateTime(final LocalDateTime temporal) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(temporal);
    }

    public String formatLocalTimeRange(final ZonedDateTime start, final ZonedDateTime end) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(start))
                + " – "
                + PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(end));
    }

    public String formatLocalTimeRange(final OffsetDateTime start, final OffsetDateTime end) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(start))
                + " – "
                + PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(end));
    }

    public String formatLocalTimeRange(final Instant start, final Instant end) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(start))
                + " – "
                + PRETTY_LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(end));
    }

    public String formatLocalTimeRange(final LocalDateTime start, final LocalDateTime end) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(start)
                + " – "
                + PRETTY_LOCAL_TIME_FORMATTER.format(end);
    }

    public String formatLocalTimeRange(final LocalTime start, final LocalTime end) {
        return PRETTY_LOCAL_TIME_FORMATTER.format(start)
                + " – "
                + PRETTY_LOCAL_TIME_FORMATTER.format(end);
    }

    public String formatLocalDateRange(final ZonedDateTime start, final ZonedDateTime end) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(start))
                + " – "
                + PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(end));
    }

    public String formatLocalDateRange(final OffsetDateTime start, final OffsetDateTime end) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(start))
                + " – "
                + PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(end));
    }

    public String formatLocalDateRange(final Instant start, final Instant end) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(start))
                + " – "
                + PRETTY_LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(end));
    }

    public String formatLocalDateRange(final LocalDateTime start, final LocalDateTime end) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(start)
                + " – "
                + PRETTY_LOCAL_DATE_FORMATTER.format(end);
    }

    public String formatLocalDateRange(final LocalDate start, final LocalDate end) {
        return PRETTY_LOCAL_DATE_FORMATTER.format(start)
                + " – "
                + PRETTY_LOCAL_DATE_FORMATTER.format(end);
    }

    public String formatLocalDateTimeRange(final ZonedDateTime start, final ZonedDateTime end) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(start))
                + " – "
                + PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(end));
    }

    public String formatLocalDateTimeRange(final OffsetDateTime start, final OffsetDateTime end) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(start))
                + " – "
                + PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(end));
    }

    public String formatLocalDateTimeRange(final Instant start, final Instant end) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(start))
                + " – "
                + PRETTY_LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(end));
    }

    public String formatLocalDateTimeRange(final LocalDateTime start, final LocalDateTime end) {
        return PRETTY_LOCAL_DATE_TIME_FORMATTER.format(start)
                + " – "
                + PRETTY_LOCAL_DATE_TIME_FORMATTER.format(end);
    }
}
