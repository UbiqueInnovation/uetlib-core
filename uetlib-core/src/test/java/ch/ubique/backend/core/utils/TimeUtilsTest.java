package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.TimeUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import ch.ubique.backend.core.assertion.AssertionUtils;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

class TimeUtilsTest {

    @Test
    void now() {
        final var expected = ZonedDateTime.now(ZONE_ID_EUROPE_BERLIN);
        final var actual = TIME_UTILS_EUROPE_BERLIN.now();
        assertTrue(expected.isBefore(actual));
        assertTrue(actual.minusSeconds(1L).isBefore(expected));
        assertTrue(expected.minusSeconds(1L).isBefore(actual));
    }

    @Test
    void nowEuZh() {
        final var expected = ZonedDateTime.now(ZONE_ID_EUROPE_ZURICH);
        final var actual = TIME_UTILS_EUROPE_ZURICH.now();
        assertTrue(expected.isBefore(actual));
        assertTrue(actual.minusSeconds(1L).isBefore(expected));
        assertTrue(expected.minusSeconds(1L).isBefore(actual));
    }

    @Test
    void ofMillisEuZh() {
        final var expected =
                ZonedDateTime.now(ZONE_ID_EUROPE_ZURICH).truncatedTo(ChronoUnit.MILLIS);
        final var actual = TIME_UTILS_EUROPE_ZURICH.now();
        assertEquals(
                expected, TIME_UTILS_EUROPE_ZURICH.ofMillis(expected.toInstant().toEpochMilli()));
        assertTrue(actual.minusSeconds(1).isBefore(expected));
    }

    @Test
    void ofLocalDateLocalTimeEuZh() {
        final var localDate = LocalDate.of(2022, 3, 24);
        final var localTime = LocalTime.of(0, 9, 40);
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(
                zonedDateTime, TIME_UTILS_EUROPE_ZURICH.ofLocalDateLocalTime(localDate, localTime));
    }

    @Test
    void toLocalDateEuZh() {
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var localDate = LocalDate.of(2022, 3, 24);
        assertEquals(localDate, TIME_UTILS_EUROPE_ZURICH.toLocalDate(zonedDateTime));
    }

    @Test
    void toLocalTimeEuZh() {
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var localTime = LocalTime.of(0, 9, 40);
        assertEquals(localTime, TIME_UTILS_EUROPE_ZURICH.toLocalTime(zonedDateTime));
    }

    @Test
    void toLocalDateTimeEuZh() {
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        final var localDateTime = LocalDateTime.of(2022, 3, 24, 0, 9, 40);
        assertEquals(localDateTime, TIME_UTILS_EUROPE_ZURICH.toLocalDateTime(zonedDateTime));
    }

    @Test
    void floorToZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 55, 19, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 55, 15, 0, ZONE_ID_UTC),
                floorTo(input, ChronoField.SECOND_OF_MINUTE, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 55, 15, 0, ZONE_ID_UTC),
                floorTo(input, ChronoField.SECOND_OF_DAY, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                floorTo(input, ChronoField.MINUTE_OF_HOUR, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 0, 0, 0, 0, ZONE_ID_UTC),
                floorTo(input, ChronoField.HOUR_OF_DAY, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 15, 0, 0, 0, 0, ZONE_ID_UTC),
                floorTo(input, ChronoField.DAY_OF_MONTH, 15));
    }

    @Test
    void floorToDuration() {
        final var input = Duration.of(2100, ChronoUnit.SECONDS);
        assertEquals(
                Duration.of(2000, ChronoUnit.SECONDS), floorTo(input, ChronoUnit.SECONDS, 1000));
        assertEquals(
                Duration.of(2100, ChronoUnit.SECONDS),
                floorTo(input, ChronoUnit.SECONDS, 15 * 140));
        assertEquals(
                Duration.of(2096, ChronoUnit.SECONDS), floorTo(input, ChronoUnit.SECONDS, 8 * 262));
    }

    @Test
    void ceilToZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 55, 19, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 55, 30, 0, ZONE_ID_UTC),
                ceilTo(input, ChronoField.SECOND_OF_MINUTE, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 55, 30, 0, ZONE_ID_UTC),
                ceilTo(input, ChronoField.SECOND_OF_DAY, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                ceilTo(input, ChronoField.MINUTE_OF_HOUR, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                ceilTo(input, ChronoField.MINUTE_OF_HOUR, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 15, 0, 0, 0, ZONE_ID_UTC),
                ceilTo(input, ChronoField.HOUR_OF_DAY, 15));
        assertEquals(
                ZonedDateTime.of(2022, 4, 30, 0, 0, 0, 0, ZONE_ID_UTC),
                ceilTo(input, ChronoField.DAY_OF_MONTH, 15));

        final var input2 = ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                ceilTo(input2, ChronoField.MINUTE_OF_HOUR, 15));
    }

    @Test
    void ceilToDuration() {
        final var input = Duration.of(2100, ChronoUnit.SECONDS);
        assertEquals(
                Duration.of(3000, ChronoUnit.SECONDS), ceilTo(input, ChronoUnit.SECONDS, 1000));
        assertEquals(
                Duration.of(2100, ChronoUnit.SECONDS), ceilTo(input, ChronoUnit.SECONDS, 15 * 140));
        assertEquals(
                Duration.of(4192, ChronoUnit.SECONDS), ceilTo(input, ChronoUnit.SECONDS, 8 * 262));
    }

    @Test
    void floorToQuarterHoursZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 0, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToQuarterHours(input));
        final var input2 = ZonedDateTime.of(2022, 4, 29, 8, 14, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC),
                floorToQuarterHours(input2));
        final var input3 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 15, 0, 0, ZONE_ID_UTC),
                floorToQuarterHours(input3));
        final var input4 = ZonedDateTime.of(2022, 4, 29, 8, 31, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 30, 0, 0, ZONE_ID_UTC),
                floorToQuarterHours(input4));
        final var input5 = ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                floorToQuarterHours(input5));
        final var input6 = ZonedDateTime.of(2022, 4, 29, 8, 45, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                floorToQuarterHours(input6));
        final var input7 = ZonedDateTime.of(2022, 4, 29, 8, 59, 59, 999999999, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                floorToQuarterHours(input7));
    }

    @Test
    void floorToQuarterHoursDuration() {
        final var input = Duration.of(2100, ChronoUnit.SECONDS);
        assertEquals(Duration.of(1800, ChronoUnit.SECONDS), floorToQuarterHours(input));
        final var input2 = Duration.of(1801, ChronoUnit.SECONDS);
        assertEquals(Duration.of(1800, ChronoUnit.SECONDS), floorToQuarterHours(input2));
        final var input3 = Duration.of(2699, ChronoUnit.SECONDS);
        assertEquals(Duration.of(1800, ChronoUnit.SECONDS), floorToQuarterHours(input3));
        final var input4 = Duration.of(-1, ChronoUnit.SECONDS);
        assertEquals(Duration.of(0, ChronoUnit.SECONDS), floorToQuarterHours(input4));
        final var input5 = Duration.of(0, ChronoUnit.SECONDS);
        assertEquals(Duration.of(0, ChronoUnit.SECONDS), floorToQuarterHours(input5));
        final var input6 = Duration.of(-1000, ChronoUnit.SECONDS);
        assertEquals(Duration.of(-900, ChronoUnit.SECONDS), floorToQuarterHours(input6));
    }

    @Test
    void ceilToQuarterHoursZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 0, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 15, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToQuarterHours(input));
        final var input2 = ZonedDateTime.of(2022, 4, 29, 8, 14, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 15, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToQuarterHours(input2));
        final var input3 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 30, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToQuarterHours(input3));
        final var input4 = ZonedDateTime.of(2022, 4, 29, 8, 31, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToQuarterHours(input4));
        final var input5 = ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToQuarterHours(input5));
        final var input6 = ZonedDateTime.of(2022, 4, 29, 8, 45, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToQuarterHours(input6));
        final var input7 = ZonedDateTime.of(2022, 4, 29, 8, 59, 59, 999999999, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToQuarterHours(input7));
    }

    @Test
    void ceilToQuarterHoursDuration() {
        final var input = Duration.of(2100, ChronoUnit.SECONDS);
        assertEquals(Duration.of(2700, ChronoUnit.SECONDS), TimeUtils.ceilToQuarterHours(input));
        final var input2 = Duration.of(1801, ChronoUnit.SECONDS);
        assertEquals(Duration.of(2700, ChronoUnit.SECONDS), TimeUtils.ceilToQuarterHours(input2));
        final var input3 = Duration.of(2699, ChronoUnit.SECONDS);
        assertEquals(Duration.of(2700, ChronoUnit.SECONDS), TimeUtils.ceilToQuarterHours(input3));
        final var input4 = Duration.of(-1, ChronoUnit.SECONDS);
        assertEquals(Duration.of(900, ChronoUnit.SECONDS), TimeUtils.ceilToQuarterHours(input4));
        final var input5 = Duration.of(0, ChronoUnit.SECONDS);
        assertEquals(Duration.of(0, ChronoUnit.SECONDS), TimeUtils.ceilToQuarterHours(input5));
        final var input6 = Duration.of(-1000, ChronoUnit.SECONDS);
        assertEquals(Duration.of(0, ChronoUnit.SECONDS), TimeUtils.ceilToQuarterHours(input6));
    }

    @Test
    void ceilToHoursZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 0, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToHours(input));
        final var input2 = ZonedDateTime.of(2022, 4, 29, 8, 14, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToHours(input2));
        final var input3 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToHours(input3));
        final var input4 = ZonedDateTime.of(2022, 4, 29, 8, 31, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToHours(input4));
        final var input5 = ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToHours(input5));
        final var input6 = ZonedDateTime.of(2022, 4, 29, 8, 45, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToHours(input6));
        final var input7 = ZonedDateTime.of(2022, 4, 29, 8, 59, 59, 999999999, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC),
                TimeUtils.ceilToHours(input7));
    }

    @Test
    void ceilToHoursDuration() {
        final var input = Duration.of(4000, ChronoUnit.SECONDS);
        assertEquals(Duration.of(7200, ChronoUnit.SECONDS), TimeUtils.ceilToHours(input));
        final var input2 = Duration.of(3601, ChronoUnit.SECONDS);
        assertEquals(Duration.of(7200, ChronoUnit.SECONDS), TimeUtils.ceilToHours(input2));
        final var input3 = Duration.of(7199, ChronoUnit.SECONDS);
        assertEquals(Duration.of(7200, ChronoUnit.SECONDS), TimeUtils.ceilToHours(input3));
        final var input4 = Duration.of(0, ChronoUnit.SECONDS);
        assertEquals(Duration.of(0, ChronoUnit.SECONDS), TimeUtils.ceilToHours(input4));
        final var input5 = Duration.of(3600, ChronoUnit.SECONDS);
        assertEquals(Duration.of(3600, ChronoUnit.SECONDS), TimeUtils.ceilToHours(input5));
        final var input6 = Duration.of(1, ChronoUnit.SECONDS);
        assertEquals(Duration.of(3600, ChronoUnit.SECONDS), TimeUtils.ceilToHours(input6));
    }

    @Test
    void floorToHoursZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 0, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToHours(input));
        final var input2 = ZonedDateTime.of(2022, 4, 29, 8, 14, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToHours(input2));
        final var input3 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToHours(input3));
        final var input4 = ZonedDateTime.of(2022, 4, 29, 8, 31, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToHours(input4));
        final var input5 = ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToHours(input5));
        final var input6 = ZonedDateTime.of(2022, 4, 29, 8, 45, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToHours(input6));
        final var input7 = ZonedDateTime.of(2022, 4, 29, 8, 59, 59, 999999999, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 8, 0, 0, 0, ZONE_ID_UTC), floorToHours(input7));
    }

    @Test
    void floorToHoursDuration() {
        final var input = Duration.of(4000, ChronoUnit.SECONDS);
        assertEquals(Duration.of(3600, ChronoUnit.SECONDS), floorToHours(input));
        final var input2 = Duration.of(3601, ChronoUnit.SECONDS);
        assertEquals(Duration.of(3600, ChronoUnit.SECONDS), floorToHours(input2));
        final var input3 = Duration.of(7199, ChronoUnit.SECONDS);
        assertEquals(Duration.of(3600, ChronoUnit.SECONDS), floorToHours(input3));
        final var input4 = Duration.of(0, ChronoUnit.SECONDS);
        assertEquals(Duration.of(0, ChronoUnit.SECONDS), floorToHours(input4));
        final var input5 = Duration.of(3600, ChronoUnit.SECONDS);
        assertEquals(Duration.of(3600, ChronoUnit.SECONDS), floorToHours(input5));
        final var input6 = Duration.of(1, ChronoUnit.SECONDS);
        assertEquals(Duration.of(0, ChronoUnit.SECONDS), floorToHours(input6));
    }

    @Test
    void durationToHours() {
        assertEquals(0.25, durationToHoursDouble(Duration.of(900, ChronoUnit.SECONDS)));
        assertEquals(1.0, durationToHoursDouble(Duration.of(3600, ChronoUnit.SECONDS)));
        assertEquals(0.0, durationToHoursDouble(Duration.of(0, ChronoUnit.SECONDS)));
    }

    @Test
    void startOfDay() {
        final var now = TIME_UTILS_EUROPE_ZURICH.now();
        final var startOfDay = TIME_UTILS_EUROPE_ZURICH.startOfDay(now);
        // at beginning of day
        assertEquals(0, startOfDay.get(ChronoField.MILLI_OF_SECOND));
        assertEquals(0, startOfDay.get(ChronoField.SECOND_OF_MINUTE));
        assertEquals(0, startOfDay.get(ChronoField.MINUTE_OF_HOUR));
        assertEquals(0, startOfDay.get(ChronoField.HOUR_OF_DAY));
        // same day
        assertEquals(now.getDayOfYear(), startOfDay.getDayOfYear());
    }

    @Test
    void startOfMonth() {
        assertEquals(
                TIME_UTILS_EUROPE_ZURICH.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS),
                TIME_UTILS_EUROPE_ZURICH.startOfMonth());
        assertEquals(
                TIME_UTILS_UTC.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS),
                TIME_UTILS_UTC.startOfMonth());
    }

    @Test
    void startOfNextMonth() {
        assertEquals(
                TIME_UTILS_EUROPE_ZURICH
                        .now()
                        .withDayOfMonth(1)
                        .truncatedTo(ChronoUnit.DAYS)
                        .plusMonths(1),
                TIME_UTILS_EUROPE_ZURICH.startOfNextMonth());
        assertEquals(
                TIME_UTILS_UTC.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS).plusMonths(1),
                TIME_UTILS_UTC.startOfNextMonth());
    }

    @Test
    void isAfterOrEqual() {
        final var time1 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        final var time2 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000001, ZONE_ID_UTC);
        final var time3 = ZonedDateTime.of(2023, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);

        assertTrue(TimeUtils.isAfterOrEqual(time3, time1));
        assertTrue(TimeUtils.isAfterOrEqual(time2, time1));
        assertTrue(TimeUtils.isAfterOrEqual(time1, time1));
        assertFalse(TimeUtils.isAfterOrEqual(time1, time3));
        assertFalse(TimeUtils.isAfterOrEqual(time1, time2));

        AssertionUtils.assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isAfterOrEqual(time1, null),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        //noinspection ConstantConditions
        AssertionUtils.assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isAfterOrEqual(null, time1),
                "Cannot invoke"
                        + " \"java.time.ZonedDateTime.isBefore(java.time.chrono.ChronoZonedDateTime)\""
                        + " because \"time1\" is null");
    }

    @Test
    void isBeforeOrEqual() {
        final var time1 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        final var time2 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000001, ZONE_ID_UTC);
        final var time3 = ZonedDateTime.of(2023, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);

        assertTrue(TimeUtils.isBeforeOrEqual(time1, time3));
        assertTrue(TimeUtils.isBeforeOrEqual(time1, time2));
        assertTrue(TimeUtils.isBeforeOrEqual(time1, time1));
        assertFalse(TimeUtils.isBeforeOrEqual(time3, time1));
        assertFalse(TimeUtils.isBeforeOrEqual(time2, time1));

        AssertionUtils.assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isBeforeOrEqual(time1, null),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        //noinspection ConstantConditions
        AssertionUtils.assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isBeforeOrEqual(null, time1),
                "Cannot invoke"
                        + " \"java.time.ZonedDateTime.isAfter(java.time.chrono.ChronoZonedDateTime)\""
                        + " because \"time1\" is null");
    }

    @Test
    void isBetween() {
        final var time1 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        final var time2 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000001, ZONE_ID_UTC);
        final var time3 = ZonedDateTime.of(2023, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);

        assertTrue(TimeUtils.isBetween(time2, time1, time3));
        assertTrue(TimeUtils.isBetween(time1, time1, time2));
        assertFalse(TimeUtils.isBetween(time3, time1, time3));
        assertFalse(TimeUtils.isBetween(time1, time1, time1));

        // does not throw as the first check to compare time1 and time3 already returns false
        assertDoesNotThrow(() -> TimeUtils.isBetween(time1, time3, null));
        AssertionUtils.assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isBetween(time3, time1, null),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        AssertionUtils.assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isBetween(time1, null, time3),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        //noinspection ConstantConditions
        AssertionUtils.assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isBetween(null, time1, time3),
                "Cannot invoke"
                        + " \"java.time.ZonedDateTime.isBefore(java.time.chrono.ChronoZonedDateTime)\""
                        + " because \"time\" is null");
    }

    @Test
    void isMidnightEuZh() {
        assertTrue(
                TIME_UTILS_EUROPE_ZURICH.isMidnight(
                        ZonedDateTime.of(2022, 4, 29, 0, 0, 0, 0, ZONE_ID_EUROPE_ZURICH)));
        assertFalse(
                TIME_UTILS_EUROPE_ZURICH.isMidnight(
                        ZonedDateTime.of(2022, 4, 29, 0, 0, 0, 0, ZONE_ID_UTC)));

        assertFalse(
                TIME_UTILS_EUROPE_ZURICH.isMidnight(
                        ZonedDateTime.of(2022, 4, 29, 22, 0, 0, 0, ZONE_ID_EUROPE_ZURICH)));
        assertTrue(
                TIME_UTILS_EUROPE_ZURICH.isMidnight(
                        ZonedDateTime.of(2022, 4, 29, 22, 0, 0, 0, ZONE_ID_UTC)));
    }

    @Test
    void startOfTodayEuZh() {
        final var startOfTodayEuZh = TIME_UTILS_EUROPE_ZURICH.startOfToday();
        final var nowEuZh = TIME_UTILS_EUROPE_ZURICH.now();
        assertEquals(nowEuZh.truncatedTo(ChronoUnit.DAYS), startOfTodayEuZh);
    }

    @Test
    void startOfDayEuZh() {
        final var startOfTodayEuZh = TIME_UTILS_EUROPE_ZURICH.startOfToday();
        final var nowEuZh = TIME_UTILS_EUROPE_ZURICH.now();
        assertEquals(startOfTodayEuZh, TIME_UTILS_EUROPE_ZURICH.startOfDay(nowEuZh));
    }

    @Test
    void ceilToHours() {
        final var startOfTodayEuZh = TIME_UTILS_EUROPE_ZURICH.startOfToday();
        assertEquals(
                Duration.of(7200, ChronoUnit.SECONDS),
                TimeUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh.plusHours(2)));
        assertEquals(
                Duration.of(14400, ChronoUnit.SECONDS),
                TimeUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh.plusMinutes(200)));
        assertEquals(
                Duration.of(3600, ChronoUnit.SECONDS),
                TimeUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh.plusSeconds(60 * 35)));
        assertEquals(
                Duration.of(0, ChronoUnit.SECONDS),
                TimeUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh));
    }

    @Test
    void ceilToQuarterHours() {
        final var startOfTodayEuZh = TIME_UTILS_EUROPE_ZURICH.startOfToday();
        assertEquals(
                Duration.of(7200, ChronoUnit.SECONDS),
                TimeUtils.ceilToQuarterHours(startOfTodayEuZh, startOfTodayEuZh.plusHours(2)));
        assertEquals(
                Duration.of(12600, ChronoUnit.SECONDS),
                TimeUtils.ceilToQuarterHours(startOfTodayEuZh, startOfTodayEuZh.plusMinutes(200)));
        assertEquals(
                Duration.of(2700, ChronoUnit.SECONDS),
                TimeUtils.ceilToQuarterHours(
                        startOfTodayEuZh, startOfTodayEuZh.plusSeconds(60 * 35)));
        assertEquals(
                Duration.of(0, ChronoUnit.SECONDS),
                TimeUtils.ceilToQuarterHours(startOfTodayEuZh, startOfTodayEuZh));
    }
}
