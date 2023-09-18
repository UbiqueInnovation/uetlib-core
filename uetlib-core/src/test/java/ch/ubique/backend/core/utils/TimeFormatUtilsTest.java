package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.TimeFormatUtils.TIME_FORMAT_UTILS_EUROPE_ZURICH;
import static ch.ubique.backend.core.utils.TimeUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.*;

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
                "24.03.2022 00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTime(offsetDateTime));

        final var instant = Instant.ofEpochSecond(1648076980);
        assertEquals("24.03.2022 00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTime(instant));

        final var localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                "24.03.2022 00:09", TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTime(localDateTime));
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
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateRange(startZonedDateTime, endZonedDateTime));

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
                TIME_FORMAT_UTILS_EUROPE_ZURICH.formatLocalDateTimeRange(startZonedDateTime, endZonedDateTime));

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
}
