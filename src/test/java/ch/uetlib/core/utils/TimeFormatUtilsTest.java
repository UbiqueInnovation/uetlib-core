package ch.uetlib.core.utils;

import static ch.uetlib.core.utils.TimeFormatUtils.TIME_FORMAT_UTILS_EUROPE_ZURICH;
import static ch.uetlib.core.utils.TimeFormatUtils.TIME_FORMAT_UTILS_UTC;
import static ch.uetlib.core.utils.TimeUtils.*;
import static ch.uetlib.test.assertion.AssertionUtils.assertThrowsWithMessage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeParseException;

@SuppressWarnings("ResultOfMethodCallIgnored")
class TimeFormatUtilsTest {

    @Test
    void formatLocalDate() {
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("24.03.2022", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDate(zonedDateTime));

        final var offsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("24.03.2022", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDate(offsetDateTime));

        final var instant = Instant.ofEpochSecond(1648076980);
        assertEquals("24.03.2022", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDate(instant));

        final var localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("24.03.2022", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDate(localDateTime));

        final var localDate =
                LocalDate.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("24.03.2022", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDate(localDate));
    }

    @Test
    void formatLocalTime() {
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTime(zonedDateTime));

        final var offsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTime(offsetDateTime));

        final var instant = Instant.ofEpochSecond(1648076980);
        assertEquals("00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTime(instant));

        final var localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTime(localDateTime));

        final var localDate =
                LocalTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals("00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTime(localDate));
    }

    @Test
    void formatLocalDateTime() {
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 00:09",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTime(zonedDateTime));

        final var offsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 00:09",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTime(offsetDateTime));

        final var instant = Instant.ofEpochSecond(1648076980);
        assertEquals(
                "24.03.2022 00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTime(instant));

        final var localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 00:09",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTime(localDateTime));
    }

    @Test
    void formatLocalTimeRange() {
        final var startZonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endZonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648081180), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "00:09 – 01:19",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTimeRange(
                        startZonedDateTime, endZonedDateTime));

        final var startOffsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endOffsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648081180), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "00:09 – 01:19",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTimeRange(
                        startOffsetDateTime, endOffsetDateTime));

        final var startInstant = Instant.ofEpochSecond(1648076980);
        final var endInstant = Instant.ofEpochSecond(1648081180);
        assertEquals(
                "00:09 – 01:19",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTimeRange(startInstant, endInstant));

        final var startLocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endLocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648081180), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "00:09 – 01:19",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTimeRange(
                        startLocalDateTime, endLocalDateTime));

        final var startLocalTime =
                LocalTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endLocalTime =
                LocalTime.ofInstant(Instant.ofEpochSecond(1648081180), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "00:09 – 01:19",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalTimeRange(startLocalTime, endLocalTime));
    }

    @Test
    void formatLocalDateRange() {
        final var startZonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endZonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648276980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 – 26.03.2022",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateRange(
                        startZonedDateTime, endZonedDateTime));

        final var startOffsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endOffsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648276980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 – 26.03.2022",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateRange(
                        startOffsetDateTime, endOffsetDateTime));

        final var startInstant = Instant.ofEpochSecond(1648076980);
        final var endInstant = Instant.ofEpochSecond(1648276980);
        assertEquals(
                "24.03.2022 – 26.03.2022",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateRange(startInstant, endInstant));

        final var startLocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endLocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648276980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 – 26.03.2022",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateRange(
                        startLocalDateTime, endLocalDateTime));

        final var startLocalDate =
                LocalDate.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endLocalDate =
                LocalDate.ofInstant(Instant.ofEpochSecond(1648276980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 – 26.03.2022",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateRange(startLocalDate, endLocalDate));
    }

    @Test
    void formatLocalDateTimeRange() {
        final var startZonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endZonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648276980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 00:09 – 26.03.2022 07:43",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTimeRange(
                        startZonedDateTime, endZonedDateTime));

        final var startOffsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endOffsetDateTime =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(1648276980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 00:09 – 26.03.2022 07:43",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTimeRange(
                        startOffsetDateTime, endOffsetDateTime));

        final var startInstant = Instant.ofEpochSecond(1648076980);
        final var endInstant = Instant.ofEpochSecond(1648276980);
        assertEquals(
                "24.03.2022 00:09 – 26.03.2022 07:43",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTimeRange(startInstant, endInstant));

        final var startLocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var endLocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648276980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 00:09 – 26.03.2022 07:43",
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTimeRange(
                        startLocalDateTime, endLocalDateTime));
    }

    @Test
    void getInstance() {
        assertEquals(
                TIME_FORMAT_UTILS_EUROPE_ZURICH,
                TimeFormatUtils.getInstance(ZONE_ID_EUROPE_ZURICH));
        assertEquals(
                TIME_FORMAT_UTILS_EUROPE_ZURICH,
                TimeFormatUtils.getInstance(ZONE_NAME_EUROPE_ZURICH));

        final var americaNewYork = TimeFormatUtils.getInstance("America/New_York");
        assertEquals(americaNewYork, TimeFormatUtils.getInstance("America/New_York"));
        assertEquals(americaNewYork, TimeFormatUtils.getInstance(ZoneId.of("America/New_York")));
    }

    @Test
    void parsePrettyLocalDate() {
        assertEquals(LocalDate.of(2023, 9, 26), TimeFormatUtils.parsePrettyLocalDate("26.09.2023"));
        assertEquals(
                LocalDate.of(2023, 9, 30),
                TimeFormatUtils.parsePrettyLocalDate("31.09.2023")); // somehow this is legal, lol

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeFormatUtils.parsePrettyLocalDate(null),
                "text");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDate(""),
                "Text '' could not be parsed at index 0");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDate("26.9.2023"),
                "Text '26.9.2023' could not be parsed at index 3");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDate("32.09.2023"),
                "Text '32.09.2023' could not be parsed: Invalid value for DayOfMonth (valid values"
                        + " 1 - 28/31): 32");
    }

    @Test
    void parsePrettyLocalTime() {
        assertEquals(LocalTime.of(8, 6), TimeFormatUtils.parsePrettyLocalTime("08:06"));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeFormatUtils.parsePrettyLocalTime(null),
                "text");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalTime(""),
                "Text '' could not be parsed at index 0");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalTime("08:6"),
                "Text '08:6' could not be parsed at index 3");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalTime("8:06"),
                "Text '8:06' could not be parsed at index 0");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalTime("24:06"),
                "Text '24:06' could not be parsed: Invalid value for HourOfDay (valid values 0 -"
                        + " 23): 24");
    }

    @Test
    void parsePrettyLocalDateTime() {
        assertEquals(
                LocalDateTime.of(LocalDate.of(2023, 9, 26), LocalTime.of(8, 6)),
                TimeFormatUtils.parsePrettyLocalDateTime("26.09.2023 08:06"));
        assertEquals(
                LocalDateTime.of(LocalDate.of(2023, 9, 30), LocalTime.of(8, 6)),
                TimeFormatUtils.parsePrettyLocalDateTime(
                        "31.09.2023 08:06")); // somehow this is legal, lol

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeFormatUtils.parsePrettyLocalDateTime(null),
                "text");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDateTime(""),
                "Text '' could not be parsed at index 0");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDateTime("26.9.2023 08:06"),
                "Text '26.9.2023 08:06' could not be parsed at index 3");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDateTime("32.09.2023 08:06"),
                "Text '32.09.2023 08:06' could not be parsed: Invalid value for DayOfMonth (valid"
                        + " values 1 - 28/31): 32");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDateTime("26.09.2023 08:6"),
                "Text '26.09.2023 08:6' could not be parsed at index 14");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TimeFormatUtils.parsePrettyLocalDateTime("26.09.2023 8:06"),
                "Text '26.09.2023 8:06' could not be parsed at index 11");
    }

    @Test
    void parsePrettyLocalDateLocalTimeAtZone() {
        assertEquals(
                ZonedDateTime.of(LocalDate.of(2023, 9, 26), LocalTime.of(8, 6), ZONE_ID_UTC),
                TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone("26.09.2023", "08:06"));
        assertEquals(
                ZonedDateTime.of(LocalDate.of(2023, 9, 30), LocalTime.of(8, 6), ZONE_ID_UTC),
                TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone(
                        "31.09.2023", "08:06")); // somehow this is legal, lol

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone(null, null),
                "text");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone("", ""),
                "Text '' could not be parsed at index 0");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () ->
                        TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone(
                                "26.9.2023", "08:06"),
                "Text '26.9.2023' could not be parsed at index 3");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () ->
                        TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone(
                                "32.09.2023", "08:06"),
                "Text '32.09.2023' could not be parsed: Invalid value for DayOfMonth (valid"
                        + " values 1 - 28/31): 32");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () ->
                        TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone(
                                "26.09.2023", "08:6"),
                "Text '08:6' could not be parsed at index 3");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () ->
                        TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateLocalTimeAtZone(
                                "26.09.2023", "8:06"),
                "Text '8:06' could not be parsed at index 0");
    }

    @Test
    void parsePrettyLocalDateTimeAtZone() {
        assertEquals(
                ZonedDateTime.of(
                        LocalDateTime.of(LocalDate.of(2023, 9, 26), LocalTime.of(8, 6)),
                        ZONE_ID_UTC),
                TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone("26.09.2023 08:06"));
        assertEquals(
                ZonedDateTime.of(
                        LocalDateTime.of(LocalDate.of(2023, 9, 30), LocalTime.of(8, 6)),
                        ZONE_ID_UTC),
                TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone(
                        "31.09.2023 08:06")); // somehow this is legal, lol

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone(null),
                "text");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone(""),
                "Text '' could not be parsed at index 0");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone("26.9.2023 08:06"),
                "Text '26.9.2023 08:06' could not be parsed at index 3");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone("32.09.2023 08:06"),
                "Text '32.09.2023 08:06' could not be parsed: Invalid value for DayOfMonth (valid"
                        + " values 1 - 28/31): 32");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone("26.09.2023 08:6"),
                "Text '26.09.2023 08:6' could not be parsed at index 14");
        assertThrowsWithMessage(
                DateTimeParseException.class,
                () -> TIME_FORMAT_UTILS_UTC.parsePrettyLocalDateTimeAtZone("26.09.2023 8:06"),
                "Text '26.09.2023 8:06' could not be parsed at index 11");
    }
}
