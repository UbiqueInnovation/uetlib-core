package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.TimeUtils.*;

import jakarta.validation.constraints.NotNull;

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
                    Map.ofEntries(
                            Map.entry(ZONE_ID_UTC, TIME_FORMAT_UTILS_UTC),
                            Map.entry(ZONE_ID_EUROPE_ZURICH, TIME_FORMAT_UTILS_EUROPE_ZURICH),
                            Map.entry(ZONE_ID_EUROPE_BERLIN, TIME_FORMAT_UTILS_EUROPE_BERLIN)));

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter LOCAL_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @NotNull private final TimeUtils timeUtils;

    @Contract(pure = true)
    private TimeFormatUtils(@NotNull final TimeUtils timeUtils) {
        this.timeUtils = timeUtils;
    }

    public static TimeFormatUtils getInstance(@NotNull final ZoneId zoneId) {
        final var normalized = zoneId.normalized();
        return INSTANCES.computeIfAbsent(
                normalized, zi -> new TimeFormatUtils(TimeUtils.getInstance(zi)));
    }

    public static TimeFormatUtils getInstance(@NotNull final String zoneId) {
        return getInstance(ZoneId.of(zoneId));
    }

    public String formatLocalDate(final ZonedDateTime temporal) {
        return LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(temporal));
    }

    public String formatLocalDate(final OffsetDateTime temporal) {
        return LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(temporal));
    }

    public String formatLocalDate(final Instant temporal) {
        return LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(temporal));
    }

    public String formatLocalDate(final LocalDateTime temporal) {
        return LOCAL_DATE_FORMATTER.format(temporal);
    }

    public String formatLocalDate(final LocalDate temporal) {
        return LOCAL_DATE_FORMATTER.format(temporal);
    }

    public String formatLocalTime(final ZonedDateTime temporal) {
        return LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(temporal));
    }

    public String formatLocalTime(final OffsetDateTime temporal) {
        return LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(temporal));
    }

    public String formatLocalTime(final Instant temporal) {
        return LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(temporal));
    }

    public String formatLocalTime(final LocalDateTime temporal) {
        return LOCAL_TIME_FORMATTER.format(temporal);
    }

    public String formatLocalTime(final LocalTime temporal) {
        return LOCAL_TIME_FORMATTER.format(temporal);
    }

    public String formatLocalDateTime(final ZonedDateTime temporal) {
        return LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(temporal));
    }

    public String formatLocalDateTime(final OffsetDateTime temporal) {
        return LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(temporal));
    }

    public String formatLocalDateTime(final Instant temporal) {
        return LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(temporal));
    }

    public String formatLocalDateTime(final LocalDateTime temporal) {
        return LOCAL_DATE_TIME_FORMATTER.format(temporal);
    }

    public String formatLocalTimeRange(final ZonedDateTime start, final ZonedDateTime end) {
        return LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(start))
                + " – "
                + LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(end));
    }

    public String formatLocalTimeRange(final OffsetDateTime start, final OffsetDateTime end) {
        return LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(start))
                + " – "
                + LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(end));
    }

    public String formatLocalTimeRange(final Instant start, final Instant end) {
        return LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(start))
                + " – "
                + LOCAL_TIME_FORMATTER.format(timeUtils.toLocalTime(end));
    }

    public String formatLocalTimeRange(final LocalDateTime start, final LocalDateTime end) {
        return LOCAL_TIME_FORMATTER.format(start) + " – " + LOCAL_TIME_FORMATTER.format(end);
    }

    public String formatLocalTimeRange(final LocalTime start, final LocalTime end) {
        return LOCAL_TIME_FORMATTER.format(start) + " – " + LOCAL_TIME_FORMATTER.format(end);
    }

    public String formatLocalDateRange(final ZonedDateTime start, final ZonedDateTime end) {
        return LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(start))
                + " – "
                + LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(end));
    }

    public String formatLocalDateRange(final OffsetDateTime start, final OffsetDateTime end) {
        return LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(start))
                + " – "
                + LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(end));
    }

    public String formatLocalDateRange(final Instant start, final Instant end) {
        return LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(start))
                + " – "
                + LOCAL_DATE_FORMATTER.format(timeUtils.toLocalDate(end));
    }

    public String formatLocalDateRange(final LocalDateTime start, final LocalDateTime end) {
        return LOCAL_DATE_FORMATTER.format(start) + " – " + LOCAL_DATE_FORMATTER.format(end);
    }

    public String formatLocalDateRange(final LocalDate start, final LocalDate end) {
        return LOCAL_DATE_FORMATTER.format(start) + " – " + LOCAL_DATE_FORMATTER.format(end);
    }

    public String formatLocalDateTimeRange(final ZonedDateTime start, final ZonedDateTime end) {
        return LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(start))
                + " – "
                + LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(end));
    }

    public String formatLocalDateTimeRange(final OffsetDateTime start, final OffsetDateTime end) {
        return LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(start))
                + " – "
                + LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(end));
    }

    public String formatLocalDateTimeRange(final Instant start, final Instant end) {
        return LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(start))
                + " – "
                + LOCAL_DATE_TIME_FORMATTER.format(timeUtils.toLocalDateTime(end));
    }

    public String formatLocalDateTimeRange(final LocalDateTime start, final LocalDateTime end) {
        return LOCAL_DATE_TIME_FORMATTER.format(start)
                + " – "
                + LOCAL_DATE_TIME_FORMATTER.format(end);
    }
}
