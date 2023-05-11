package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.TimeUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class DurationUtilsTest {

    @Test
    void ofFractionalSeconds() {
        // TODO @dsparber
    }

    @Test
    void toFractionalSeconds() {
        assertEquals(0.25, DurationUtils.toFractionalSeconds(Duration.ofSeconds(900)));
        assertEquals(1.0, DurationUtils.toFractionalSeconds(Duration.ofSeconds(3600)));
        assertEquals(0.0, DurationUtils.toFractionalSeconds(Duration.ofSeconds(0)));
    }

    @Test
    void toFractionalHours() {
        assertEquals(0.25, DurationUtils.toFractionalHours(Duration.ofSeconds(900)));
        assertEquals(1.0, DurationUtils.toFractionalHours(Duration.ofSeconds(3600)));
        assertEquals(0.0, DurationUtils.toFractionalHours(Duration.ofSeconds(0)));
    }

    @Test
    void floorToDuration() {
        final var input = Duration.ofSeconds(2100);
        assertEquals(
                Duration.ofSeconds(2000), DurationUtils.floorTo(input, ChronoUnit.SECONDS, 1000));
        assertEquals(
                Duration.ofSeconds(2100),
                DurationUtils.floorTo(input, ChronoUnit.SECONDS, 15 * 140));
        assertEquals(
                Duration.ofSeconds(2096),
                DurationUtils.floorTo(input, ChronoUnit.SECONDS, 8 * 262));
    }

    @Test
    void ceilToDuration() {
        final var input = Duration.ofSeconds(2100);
        assertEquals(
                Duration.ofSeconds(3000), DurationUtils.ceilTo(input, ChronoUnit.SECONDS, 1000));
        assertEquals(
                Duration.ofSeconds(2100),
                DurationUtils.ceilTo(input, ChronoUnit.SECONDS, 15 * 140));
        assertEquals(
                Duration.ofSeconds(4192), DurationUtils.ceilTo(input, ChronoUnit.SECONDS, 8 * 262));
    }

    @Test
    void floorToQuarterHoursDuration() {
        final var input = Duration.ofSeconds(2100);
        assertEquals(Duration.ofSeconds(1800), DurationUtils.floorToQuarterHours(input));
        final var input2 = Duration.ofSeconds(1801);
        assertEquals(Duration.ofSeconds(1800), DurationUtils.floorToQuarterHours(input2));
        final var input3 = Duration.ofSeconds(2699);
        assertEquals(Duration.ofSeconds(1800), DurationUtils.floorToQuarterHours(input3));
        final var input4 = Duration.ofSeconds(-1);
        assertEquals(Duration.ofSeconds(0), DurationUtils.floorToQuarterHours(input4));
        final var input5 = Duration.ofSeconds(0);
        assertEquals(Duration.ofSeconds(0), DurationUtils.floorToQuarterHours(input5));
        final var input6 = Duration.ofSeconds(-1000);
        assertEquals(Duration.ofSeconds(-900), DurationUtils.floorToQuarterHours(input6));
    }

    @Test
    void ceilToQuarterHoursDuration() {
        final var input = Duration.ofSeconds(2100);
        assertEquals(Duration.ofSeconds(2700), DurationUtils.ceilToQuarterHours(input));
        final var input2 = Duration.ofSeconds(1801);
        assertEquals(Duration.ofSeconds(2700), DurationUtils.ceilToQuarterHours(input2));
        final var input3 = Duration.ofSeconds(2699);
        assertEquals(Duration.ofSeconds(2700), DurationUtils.ceilToQuarterHours(input3));
        final var input4 = Duration.ofSeconds(-1);
        assertEquals(Duration.ofSeconds(900), DurationUtils.ceilToQuarterHours(input4));
        final var input5 = Duration.ofSeconds(0);
        assertEquals(Duration.ofSeconds(0), DurationUtils.ceilToQuarterHours(input5));
        final var input6 = Duration.ofSeconds(-1000);
        assertEquals(Duration.ofSeconds(0), DurationUtils.ceilToQuarterHours(input6));
    }

    @Test
    void ceilToHoursDuration() {
        final var input = Duration.ofSeconds(4000);
        assertEquals(Duration.ofSeconds(7200), DurationUtils.ceilToHours(input));
        final var input2 = Duration.ofSeconds(3601);
        assertEquals(Duration.ofSeconds(7200), DurationUtils.ceilToHours(input2));
        final var input3 = Duration.ofSeconds(7199);
        assertEquals(Duration.ofSeconds(7200), DurationUtils.ceilToHours(input3));
        final var input4 = Duration.ofSeconds(0);
        assertEquals(Duration.ofSeconds(0), DurationUtils.ceilToHours(input4));
        final var input5 = Duration.ofSeconds(3600);
        assertEquals(Duration.ofSeconds(3600), DurationUtils.ceilToHours(input5));
        final var input6 = Duration.ofSeconds(1);
        assertEquals(Duration.ofSeconds(3600), DurationUtils.ceilToHours(input6));
    }

    @Test
    void floorToHoursDuration() {
        final var input = Duration.ofSeconds(4000);
        assertEquals(Duration.ofSeconds(3600), DurationUtils.floorToHours(input));
        final var input2 = Duration.ofSeconds(3601);
        assertEquals(Duration.ofSeconds(3600), DurationUtils.floorToHours(input2));
        final var input3 = Duration.ofSeconds(7199);
        assertEquals(Duration.ofSeconds(3600), DurationUtils.floorToHours(input3));
        final var input4 = Duration.ofSeconds(0);
        assertEquals(Duration.ofSeconds(0), DurationUtils.floorToHours(input4));
        final var input5 = Duration.ofSeconds(3600);
        assertEquals(Duration.ofSeconds(3600), DurationUtils.floorToHours(input5));
        final var input6 = Duration.ofSeconds(1);
        assertEquals(Duration.ofSeconds(0), DurationUtils.floorToHours(input6));
    }

    @Test
    void ceilToHours() {
        final var startOfTodayEuZh = TIME_UTILS_EUROPE_ZURICH.startOfToday();
        assertEquals(
                Duration.ofSeconds(7200),
                DurationUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh.plusHours(2)));
        assertEquals(
                Duration.ofSeconds(14400),
                DurationUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh.plusMinutes(200)));
        assertEquals(
                Duration.ofSeconds(3600),
                DurationUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh.plusSeconds(60 * 35)));
        assertEquals(
                Duration.ofSeconds(0),
                DurationUtils.ceilToHours(startOfTodayEuZh, startOfTodayEuZh));
    }

    @Test
    void ceilToQuarterHours() {
        final var startOfTodayEuZh = TIME_UTILS_EUROPE_ZURICH.startOfToday();
        assertEquals(
                Duration.ofSeconds(7200),
                DurationUtils.ceilToQuarterHours(startOfTodayEuZh, startOfTodayEuZh.plusHours(2)));
        assertEquals(
                Duration.ofSeconds(12600),
                DurationUtils.ceilToQuarterHours(
                        startOfTodayEuZh, startOfTodayEuZh.plusMinutes(200)));
        assertEquals(
                Duration.ofSeconds(2700),
                DurationUtils.ceilToQuarterHours(
                        startOfTodayEuZh, startOfTodayEuZh.plusSeconds(60 * 35)));
        assertEquals(
                Duration.ofSeconds(0),
                DurationUtils.ceilToQuarterHours(startOfTodayEuZh, startOfTodayEuZh));
    }
}
