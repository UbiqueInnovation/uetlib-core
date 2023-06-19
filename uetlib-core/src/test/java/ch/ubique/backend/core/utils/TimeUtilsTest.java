package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.assertion.AssertionUtils.assertThrowsWithMessage;
import static ch.ubique.backend.core.utils.TimeUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

@SuppressWarnings({"ResultOfMethodCallIgnored", "DataFlowIssue"})
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
    void ofInstantEuZh() {
        final var expected =
                ZonedDateTime.now(ZONE_ID_EUROPE_ZURICH).truncatedTo(ChronoUnit.MILLIS);
        final var actual = TIME_UTILS_EUROPE_ZURICH.now();
        assertEquals(expected, TIME_UTILS_EUROPE_ZURICH.ofInstant(expected.toInstant()));
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
    void ofLocalDateTimeEuZh() {
        final var localDateTime =
                LocalDateTime.of(LocalDate.of(2022, 3, 24), LocalTime.of(0, 9, 40));
        final var zonedDateTime =
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(1648076980), ZONE_ID_EUROPE_ZURICH);
        assertEquals(zonedDateTime, TIME_UTILS_EUROPE_ZURICH.ofLocalDateTime(localDateTime));
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
    void ceilToQuarterHoursZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 0, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 15, 0, 0, ZONE_ID_UTC), ceilToQuarterHours(input));
        final var input2 = ZonedDateTime.of(2022, 4, 29, 8, 14, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 15, 0, 0, ZONE_ID_UTC),
                ceilToQuarterHours(input2));
        final var input3 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 30, 0, 0, ZONE_ID_UTC),
                ceilToQuarterHours(input3));
        final var input4 = ZonedDateTime.of(2022, 4, 29, 8, 31, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                ceilToQuarterHours(input4));
        final var input5 = ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC),
                ceilToQuarterHours(input5));
        final var input6 = ZonedDateTime.of(2022, 4, 29, 8, 45, 14, 547000000, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToQuarterHours(input6));
        final var input7 = ZonedDateTime.of(2022, 4, 29, 8, 59, 59, 999999999, ZONE_ID_UTC);
        assertEquals(
                ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToQuarterHours(input7));
    }

    @Test
    void ceilToHoursZonedDateTime() {
        final var input = ZonedDateTime.of(2022, 4, 29, 8, 0, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToHours(input));
        final var input2 = ZonedDateTime.of(2022, 4, 29, 8, 14, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToHours(input2));
        final var input3 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToHours(input3));
        final var input4 = ZonedDateTime.of(2022, 4, 29, 8, 31, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToHours(input4));
        final var input5 = ZonedDateTime.of(2022, 4, 29, 8, 45, 0, 0, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToHours(input5));
        final var input6 = ZonedDateTime.of(2022, 4, 29, 8, 45, 14, 547000000, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToHours(input6));
        final var input7 = ZonedDateTime.of(2022, 4, 29, 8, 59, 59, 999999999, ZONE_ID_UTC);
        assertEquals(ZonedDateTime.of(2022, 4, 29, 9, 0, 0, 0, ZONE_ID_UTC), ceilToHours(input7));
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
    void isAfterOrEqualInstant() {
        final var instant1 = Instant.ofEpochSecond(1651220354, 547000000);
        final var instant2 = Instant.ofEpochSecond(1651220354, 547000001);
        final var instant3 = Instant.ofEpochSecond(1682756354, 547000000);

        assertTrue(isAfterOrEqual(instant3, instant1));
        assertTrue(isAfterOrEqual(instant2, instant1));
        assertTrue(isAfterOrEqual(instant1, instant1));
        assertFalse(isAfterOrEqual(instant1, instant3));
        assertFalse(isAfterOrEqual(instant1, instant2));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(instant1, null),
                "Cannot read field \"seconds\" because \"otherInstant\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(null, instant1),
                "Cannot invoke \"java.time.Instant.isBefore(java.time.Instant)\" because"
                        + " \"temporal1\" is null");
    }

    @Test
    void isAfterOrEqualZonedDateTime() {
        final var zonedDateTime1 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        final var zonedDateTime2 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000001, ZONE_ID_UTC);
        final var zonedDateTime3 = ZonedDateTime.of(2023, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);

        assertTrue(isAfterOrEqual(zonedDateTime3, zonedDateTime1));
        assertTrue(isAfterOrEqual(zonedDateTime2, zonedDateTime1));
        assertTrue(isAfterOrEqual(zonedDateTime1, zonedDateTime1));
        assertFalse(isAfterOrEqual(zonedDateTime1, zonedDateTime3));
        assertFalse(isAfterOrEqual(zonedDateTime1, zonedDateTime2));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(zonedDateTime1, null),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(null, zonedDateTime1),
                "Cannot invoke"
                        + " \"java.time.ZonedDateTime.isBefore(java.time.chrono.ChronoZonedDateTime)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isAfterOrEqualOffsetDateTime() {
        final var offsetDateTime1 =
                OffsetDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZoneOffset.UTC);
        final var offsetDateTime2 =
                OffsetDateTime.of(2022, 4, 29, 8, 19, 14, 547000001, ZoneOffset.UTC);
        final var offsetDateTime3 =
                OffsetDateTime.of(2023, 4, 29, 8, 19, 14, 547000000, ZoneOffset.UTC);

        assertTrue(isAfterOrEqual(offsetDateTime3, offsetDateTime1));
        assertTrue(isAfterOrEqual(offsetDateTime2, offsetDateTime1));
        assertTrue(isAfterOrEqual(offsetDateTime1, offsetDateTime1));
        assertFalse(isAfterOrEqual(offsetDateTime1, offsetDateTime3));
        assertFalse(isAfterOrEqual(offsetDateTime1, offsetDateTime2));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(offsetDateTime1, null),
                "Cannot invoke \"java.time.OffsetDateTime.toEpochSecond()\" because \"other\" is"
                        + " null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(null, offsetDateTime1),
                "Cannot invoke \"java.time.OffsetDateTime.isBefore(java.time.OffsetDateTime)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isAfterOrEqualLocalDateTime() {
        final var localDateTime1 = LocalDateTime.of(2022, 4, 29, 8, 19, 14, 547000000);
        final var localDateTime2 = LocalDateTime.of(2022, 4, 29, 8, 19, 14, 547000001);
        final var localDateTime3 = LocalDateTime.of(2023, 4, 29, 8, 19, 14, 547000000);

        assertTrue(isAfterOrEqual(localDateTime3, localDateTime1));
        assertTrue(isAfterOrEqual(localDateTime2, localDateTime1));
        assertTrue(isAfterOrEqual(localDateTime1, localDateTime1));
        assertFalse(isAfterOrEqual(localDateTime1, localDateTime3));
        assertFalse(isAfterOrEqual(localDateTime1, localDateTime2));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(localDateTime1, null),
                "Cannot invoke \"java.time.chrono.ChronoLocalDateTime.toLocalDate()\" because"
                        + " \"other\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(null, localDateTime1),
                "Cannot invoke"
                        + " \"java.time.LocalDateTime.isBefore(java.time.chrono.ChronoLocalDateTime)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isAfterOrEqualLocalDate() {
        final var localDate1 = LocalDate.of(2022, 4, 29);
        final var localDate2 = LocalDate.of(2022, 4, 30);
        final var localDate3 = LocalDate.of(2023, 4, 29);

        assertTrue(isAfterOrEqual(localDate3, localDate1));
        assertTrue(isAfterOrEqual(localDate2, localDate1));
        assertTrue(isAfterOrEqual(localDate1, localDate1));
        assertFalse(isAfterOrEqual(localDate1, localDate3));
        assertFalse(isAfterOrEqual(localDate1, localDate2));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(localDate1, null),
                "Cannot invoke \"java.time.chrono.ChronoLocalDate.toEpochDay()\" because \"other\""
                        + " is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(null, localDate1),
                "Cannot invoke \"java.time.LocalDate.isBefore(java.time.chrono.ChronoLocalDate)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isAfterOrEqualLocalTime() {

        final var localTime1 = LocalTime.of(7, 19, 14, 547000000);
        final var localTime2 = LocalTime.of(8, 19, 14, 547000001);
        final var localTime3 = LocalTime.of(8, 19, 14, 547000000);

        assertTrue(isAfterOrEqual(localTime3, localTime1));
        assertTrue(isAfterOrEqual(localTime2, localTime1));
        assertTrue(isAfterOrEqual(localTime1, localTime1));
        assertFalse(isAfterOrEqual(localTime1, localTime3));
        assertFalse(isAfterOrEqual(localTime1, localTime2));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(localTime1, null),
                "Cannot read field \"hour\" because \"other\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isAfterOrEqual(null, localTime1),
                "Cannot invoke \"java.time.LocalTime.isBefore(java.time.LocalTime)\" because"
                        + " \"temporal1\" is null");
    }

    @Test
    void isBeforeOrEqualInstant() {
        final var instant1 = Instant.ofEpochSecond(1651220354, 547000000);
        final var instant2 = Instant.ofEpochSecond(1651220354, 547000001);
        final var instant3 = Instant.ofEpochSecond(1682756354, 547000000);

        assertTrue(isBeforeOrEqual(instant1, instant3));
        assertTrue(isBeforeOrEqual(instant1, instant2));
        assertTrue(isBeforeOrEqual(instant1, instant1));
        assertFalse(isBeforeOrEqual(instant3, instant1));
        assertFalse(isBeforeOrEqual(instant2, instant1));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(instant1, null),
                "Cannot read field \"seconds\" because \"otherInstant\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(null, instant1),
                "Cannot invoke \"java.time.Instant.isAfter(java.time.Instant)\" because"
                        + " \"temporal1\" is null");
    }

    @Test
    void isBeforeOrEqualZonedDateTime() {
        final var zonedDateTime1 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);
        final var zonedDateTime2 = ZonedDateTime.of(2022, 4, 29, 8, 19, 14, 547000001, ZONE_ID_UTC);
        final var zonedDateTime3 = ZonedDateTime.of(2023, 4, 29, 8, 19, 14, 547000000, ZONE_ID_UTC);

        assertTrue(isBeforeOrEqual(zonedDateTime1, zonedDateTime3));
        assertTrue(isBeforeOrEqual(zonedDateTime1, zonedDateTime2));
        assertTrue(isBeforeOrEqual(zonedDateTime1, zonedDateTime1));
        assertFalse(isBeforeOrEqual(zonedDateTime3, zonedDateTime1));
        assertFalse(isBeforeOrEqual(zonedDateTime2, zonedDateTime1));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(zonedDateTime1, null),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(null, zonedDateTime1),
                "Cannot invoke"
                        + " \"java.time.ZonedDateTime.isAfter(java.time.chrono.ChronoZonedDateTime)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isBeforeOrEqualOffsetDateTime() {
        final var offsetDateTime1 =
                OffsetDateTime.of(2022, 4, 29, 8, 19, 14, 547000000, ZoneOffset.UTC);
        final var offsetDateTime2 =
                OffsetDateTime.of(2022, 4, 29, 8, 19, 14, 547000001, ZoneOffset.UTC);
        final var offsetDateTime3 =
                OffsetDateTime.of(2023, 4, 29, 8, 19, 14, 547000000, ZoneOffset.UTC);

        assertTrue(isBeforeOrEqual(offsetDateTime1, offsetDateTime3));
        assertTrue(isBeforeOrEqual(offsetDateTime1, offsetDateTime2));
        assertTrue(isBeforeOrEqual(offsetDateTime1, offsetDateTime1));
        assertFalse(isBeforeOrEqual(offsetDateTime3, offsetDateTime1));
        assertFalse(isBeforeOrEqual(offsetDateTime2, offsetDateTime1));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(offsetDateTime1, null),
                "Cannot invoke \"java.time.OffsetDateTime.toEpochSecond()\" because \"other\" is"
                        + " null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(null, offsetDateTime1),
                "Cannot invoke \"java.time.OffsetDateTime.isAfter(java.time.OffsetDateTime)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isBeforeOrEqualLocalDateTime() {
        final var localDateTime1 = LocalDateTime.of(2022, 4, 29, 8, 19, 14, 547000000);
        final var localDateTime2 = LocalDateTime.of(2022, 4, 29, 8, 19, 14, 547000001);
        final var localDateTime3 = LocalDateTime.of(2023, 4, 29, 8, 19, 14, 547000000);

        assertTrue(isBeforeOrEqual(localDateTime1, localDateTime3));
        assertTrue(isBeforeOrEqual(localDateTime1, localDateTime2));
        assertTrue(isBeforeOrEqual(localDateTime1, localDateTime1));
        assertFalse(isBeforeOrEqual(localDateTime3, localDateTime1));
        assertFalse(isBeforeOrEqual(localDateTime2, localDateTime1));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(localDateTime1, null),
                "Cannot invoke \"java.time.chrono.ChronoLocalDateTime.toLocalDate()\" because"
                        + " \"other\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(null, localDateTime1),
                "Cannot invoke"
                        + " \"java.time.LocalDateTime.isAfter(java.time.chrono.ChronoLocalDateTime)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isBeforeOrEqualLocalDate() {
        final var localDate1 = LocalDate.of(2022, 4, 29);
        final var localDate2 = LocalDate.of(2022, 4, 30);
        final var localDate3 = LocalDate.of(2023, 4, 29);

        assertTrue(isBeforeOrEqual(localDate1, localDate3));
        assertTrue(isBeforeOrEqual(localDate1, localDate2));
        assertTrue(isBeforeOrEqual(localDate1, localDate1));
        assertFalse(isBeforeOrEqual(localDate3, localDate1));
        assertFalse(isBeforeOrEqual(localDate2, localDate1));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(localDate1, null),
                "Cannot invoke \"java.time.chrono.ChronoLocalDate.toEpochDay()\" because \"other\""
                        + " is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(null, localDate1),
                "Cannot invoke \"java.time.LocalDate.isAfter(java.time.chrono.ChronoLocalDate)\""
                        + " because \"temporal1\" is null");
    }

    @Test
    void isBeforeOrEqualLocalTime() {
        final var localTime1 = LocalTime.of(7, 19, 14, 547000000);
        final var localTime2 = LocalTime.of(8, 19, 14, 547000001);
        final var localTime3 = LocalTime.of(8, 19, 14, 547000000);

        assertTrue(isBeforeOrEqual(localTime1, localTime3));
        assertTrue(isBeforeOrEqual(localTime1, localTime2));
        assertTrue(isBeforeOrEqual(localTime1, localTime1));
        assertFalse(isBeforeOrEqual(localTime3, localTime1));
        assertFalse(isBeforeOrEqual(localTime2, localTime1));

        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(localTime1, null),
                "Cannot read field \"hour\" because \"other\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> isBeforeOrEqual(null, localTime1),
                "Cannot invoke \"java.time.LocalTime.isAfter(java.time.LocalTime)\" because"
                        + " \"temporal1\" is null");
    }

    @Test
    void toMillis() {
        assertEquals(
                0,
                TimeUtils.toMillis(
                        ZonedDateTime.of(
                                LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZONE_ID_UTC)));
        assertEquals(
                946_758_000_000L,
                TimeUtils.toMillis(
                        ZonedDateTime.of(
                                LocalDate.of(2000, 1, 1), LocalTime.of(20, 20), ZONE_ID_UTC)));
        assertEquals(
                1_000_000_000_000L,
                TimeUtils.toMillis(
                        ZonedDateTime.of(
                                LocalDate.of(2001, 9, 9), LocalTime.of(1, 46, 40), ZONE_ID_UTC)));
        assertEquals(
                -30_610_180_800_000L,
                TimeUtils.toMillis(
                        ZonedDateTime.of(LocalDate.of(1000, 1, 1), LocalTime.NOON, ZONE_ID_UTC)));

        assertEquals(
                946_758_000_000L, TimeUtils.toMillis(TIME_UTILS_UTC.ofMillis(946_758_000_000L)));
        assertEquals(
                946_758_000_000L,
                TimeUtils.toMillis(TIME_UTILS_EUROPE_ZURICH.ofMillis(946_758_000_000L)));
        assertEquals(
                946_758_000_000L,
                TimeUtils.toMillis(TIME_UTILS_EUROPE_BERLIN.ofMillis(946_758_000_000L)));
    }

    @Test
    void min() {
        final var instant1 = Instant.ofEpochSecond(1651220354, 547000000);
        final var instant2 = Instant.ofEpochSecond(1651220354, 547000001);

        assertEquals(instant1, TimeUtils.min(instant1, instant1, null));
        assertEquals(instant1, TimeUtils.min(instant1, instant2, null));
        assertEquals(instant1, TimeUtils.min(instant2, instant1, null));
        assertEquals(instant1, TimeUtils.min(instant1, instant2));
        assertEquals(instant1, TimeUtils.min(instant1));
        assertNull(TimeUtils.min((Instant) null));
        assertNull(TimeUtils.min((Instant[]) null));

        final var zdt = ZonedDateTime.now();
        assertEquals(zdt, TimeUtils.min(zdt, zdt));
        assertEquals(zdt, TimeUtils.min(zdt.plusHours(1), zdt));

        final var oft = OffsetDateTime.now();
        assertEquals(oft, TimeUtils.min(oft, oft));
        assertEquals(oft, TimeUtils.min(oft.plusHours(1), oft));

        final var ldt = LocalDateTime.now();
        assertEquals(ldt, TimeUtils.min(ldt.plusHours(1), ldt));

        final var ld = LocalDate.now();
        assertEquals(ld, TimeUtils.min(ld.plusDays(1), ld));

        final var justAfterMidnight = LocalTime.of(0,30);
        final var justBeforeMidnight = LocalTime.of(23,30);
        assertEquals(justAfterMidnight, TimeUtils.min(justAfterMidnight.plusHours(1), justAfterMidnight));
        assertEquals(justAfterMidnight, TimeUtils.min(justBeforeMidnight.plusHours(1), justBeforeMidnight));

        final var noon = LocalTime.of(12,0);
        assertEquals(noon, TimeUtils.min(noon.plusHours(1), noon));
    }

    @Test
    void max() {
        final var instant1 = Instant.ofEpochSecond(1651220354, 547000000);
        final var instant2 = Instant.ofEpochSecond(1651220354, 547000001);

        assertEquals(instant2, TimeUtils.max(instant2, instant2, null));
        assertEquals(instant2, TimeUtils.max(instant1, instant2, null));
        assertEquals(instant2, TimeUtils.max(instant2, instant1, null));
        assertEquals(instant2, TimeUtils.max(instant2, instant1));
        assertEquals(instant2, TimeUtils.max(instant2));
        assertNull(TimeUtils.max((Instant) null));
        assertNull(TimeUtils.max((Instant[]) null));

        final var zdt = ZonedDateTime.now();
        assertEquals(zdt, TimeUtils.max(zdt, zdt));
        assertEquals(zdt, TimeUtils.max(zdt.minusHours(1), zdt));

        final var oft = OffsetDateTime.now();
        assertEquals(oft, TimeUtils.max(oft, oft));
        assertEquals(oft, TimeUtils.max(oft.minusHours(1), oft));

        final var ldt = LocalDateTime.now();
        assertEquals(ldt, TimeUtils.max(ldt.minusHours(1), ldt));

        final var ld = LocalDate.now();
        assertEquals(ld, TimeUtils.max(ld.minusDays(1), ld));

        final var justAfterMidnight = LocalTime.of(0,30);
        final var justBeforeMidnight = LocalTime.of(23,30);
        assertEquals(justBeforeMidnight, TimeUtils.max(justAfterMidnight.minusHours(1), justAfterMidnight));
        assertEquals(justBeforeMidnight, TimeUtils.max(justBeforeMidnight.minusHours(1), justBeforeMidnight));

        final var noon = LocalTime.of(12,0);
        assertEquals(noon, TimeUtils.max(noon.minusHours(1), noon));
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
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isBetween(time3, time1, null),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> TimeUtils.isBetween(time1, null, time3),
                "Cannot invoke \"java.time.chrono.ChronoZonedDateTime.toEpochSecond()\" because"
                        + " \"other\" is null");
        assertThrowsWithMessage(
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
}
