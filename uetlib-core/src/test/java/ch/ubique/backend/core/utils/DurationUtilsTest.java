package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.TimeUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class DurationUtilsTest {

    @Test
    void ofFractionalSeconds() {
        assertEquals(Duration.ofMillis(750), DurationUtils.ofFractionalSeconds(0.75));
        // 750 ms + 0.9 nanos -> truncated to 750 ms
        assertEquals(Duration.ofMillis(750), DurationUtils.ofFractionalSeconds(0.7500000009));
        // 750 ms + 1 nano -> truncated to 750 ms + 1 nano
        assertEquals(
                Duration.ofMillis(750).plusNanos(1),
                DurationUtils.ofFractionalSeconds(0.750000001));
        assertEquals(Duration.ZERO, DurationUtils.ofFractionalSeconds(0));
        assertEquals(Duration.ofMillis(2250), DurationUtils.ofFractionalSeconds(2.25));

        assertEquals(Duration.ofMillis(-1_200), DurationUtils.ofFractionalSeconds(-1.2));
    }

    @Test
    void toFractionalSeconds() {
        assertEquals(0.75, DurationUtils.toFractionalSeconds(Duration.ofMillis(750)));
        // toFractionalSeconds truncates the duration to nanos first
        assertEquals(0.75, DurationUtils.toFractionalSeconds(Duration.ofMillis(750)));
        // toFractionalSeconds truncates the duration to nanos first
        assertEquals(
                0.750000001,
                DurationUtils.toFractionalSeconds(Duration.ofMillis(750).plusNanos(1)));

        assertEquals(0, DurationUtils.toFractionalSeconds(Duration.ZERO));
        assertEquals(2.25, DurationUtils.toFractionalSeconds(Duration.ofMillis(2250)));

        assertEquals(-1.2, DurationUtils.toFractionalSeconds(Duration.ofMillis(-1_200)));
    }

    @Test
    void fractionalSeconds() {
        assertEquals(
                0.750000001,
                DurationUtils.toFractionalSeconds(DurationUtils.ofFractionalSeconds(0.750000001)));
        assertEquals(
                Duration.ofMillis(750).plusNanos(1),
                DurationUtils.ofFractionalSeconds(
                        DurationUtils.toFractionalSeconds(Duration.ofMillis(750).plusNanos(1))));

        assertEquals(
                0.75,
                DurationUtils.toFractionalSeconds(DurationUtils.ofFractionalSeconds(0.7500000009)));
        assertEquals(
                Duration.ofMillis(750),
                DurationUtils.ofFractionalSeconds(
                        DurationUtils.toFractionalSeconds(Duration.ofMillis(750))));

        assertEquals(
                -1.2, DurationUtils.toFractionalSeconds(DurationUtils.ofFractionalSeconds(-1.2)));
        assertEquals(
                Duration.ofMillis(-1_200),
                DurationUtils.ofFractionalSeconds(
                        DurationUtils.toFractionalSeconds(Duration.ofMillis(-1_200))));
    }

    @Test
    void ofFractionalHours() {
        assertEquals(Duration.ofMinutes(45), DurationUtils.ofFractionalHours(0.75));
        // 45 min + 999_999 nanos -> truncated to 45 min
        assertEquals(Duration.ofMinutes(45), DurationUtils.ofFractionalHours(0.7500002777777777));
        // 45 min + 1 milli -> truncated to 45 min + 1 milli
        assertEquals(
                Duration.ofMinutes(45).plusMillis(1),
                DurationUtils.ofFractionalHours(0.7500002777777778));

        assertEquals(Duration.ZERO, DurationUtils.ofFractionalHours(0));
        assertEquals(Duration.ofMinutes(135), DurationUtils.ofFractionalHours(2.25));

        assertEquals(Duration.ofMinutes(-72), DurationUtils.ofFractionalHours(-1.2));
    }

    @Test
    void toFractionalHours() {
        assertEquals(0.75, DurationUtils.toFractionalHours(Duration.ofMinutes(45)));
        // toFractionalHours truncates the duration to millis first
        assertEquals(
                0.75, DurationUtils.toFractionalHours(Duration.ofMinutes(45).plusNanos(999_999)));
        // toFractionalHours truncates the duration to millis first
        assertEquals(
                0.7500002777777778,
                DurationUtils.toFractionalHours(Duration.ofMinutes(45).plusNanos(1_000_000)));

        assertEquals(0, DurationUtils.toFractionalHours(Duration.ZERO));
        assertEquals(2.25, DurationUtils.toFractionalHours(Duration.ofMinutes(135)));

        assertEquals(-1.2, DurationUtils.toFractionalHours(Duration.ofMinutes(-72)));
    }

    @Test
    void fractionalHours() {
        assertEquals(
                0.7500002777777778,
                DurationUtils.toFractionalHours(
                        DurationUtils.ofFractionalHours(0.7500002777777778)));
        assertEquals(
                Duration.ofMinutes(45).plusNanos(1_000_000),
                DurationUtils.ofFractionalHours(
                        DurationUtils.toFractionalHours(
                                Duration.ofMinutes(45).plusNanos(1_000_000))));

        assertEquals(
                0.75,
                DurationUtils.toFractionalHours(
                        DurationUtils.ofFractionalHours(0.750000277777777)));
        assertEquals(
                Duration.ofMinutes(45),
                DurationUtils.ofFractionalHours(
                        DurationUtils.toFractionalHours(
                                Duration.ofMinutes(45).plusNanos(999_999))));

        assertEquals(-1.2, DurationUtils.toFractionalHours(DurationUtils.ofFractionalHours(-1.2)));
        assertEquals(
                Duration.ofMillis(-72),
                DurationUtils.ofFractionalHours(
                        DurationUtils.toFractionalHours(Duration.ofMillis(-72))));
    }

    @Test
    void floorToDuration() {
        final var input = Duration.ofSeconds(2101);
        assertEquals(
                Duration.ofSeconds(1800), DurationUtils.floorTo(input, ChronoUnit.SECONDS, 900));
        assertEquals(
                Duration.ofSeconds(2100), DurationUtils.floorTo(input, ChronoUnit.SECONDS, 15));
        assertEquals(Duration.ofSeconds(0), DurationUtils.floorTo(input, ChronoUnit.SECONDS, 2400));
    }

    @Test
    void ceilToDuration() {
        final var input = Duration.ofSeconds(2101);
        assertEquals(
                Duration.ofSeconds(2700), DurationUtils.ceilTo(input, ChronoUnit.SECONDS, 900));
        assertEquals(Duration.ofSeconds(2115), DurationUtils.ceilTo(input, ChronoUnit.SECONDS, 15));
        assertEquals(
                Duration.ofSeconds(2400), DurationUtils.ceilTo(input, ChronoUnit.SECONDS, 2400));
        assertEquals(
                Duration.ofSeconds(4800),
                DurationUtils.ceilTo(Duration.ofSeconds(2505), ChronoUnit.SECONDS, 2400));
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
