package ch.ubique.backend.core.utils.duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

class DurationBasedTemporalUnitTest {
    @Test
    void testOf() {
        assertEquals(DurationBasedTemporalUnit.of(Duration.ofDays(2)), DurationBasedTemporalUnit.ofDays(2));
        assertEquals(DurationBasedTemporalUnit.of(Duration.ofHours(2)), DurationBasedTemporalUnit.ofHours(2));
        assertEquals(DurationBasedTemporalUnit.of(Duration.ofMinutes(2)), DurationBasedTemporalUnit.ofMinutes(2));
        assertEquals(DurationBasedTemporalUnit.of(Duration.ofSeconds(2)), DurationBasedTemporalUnit.ofSeconds(2));
        assertEquals(DurationBasedTemporalUnit.of(Duration.ofMillis(2)), DurationBasedTemporalUnit.ofMillis(2));
        assertEquals(DurationBasedTemporalUnit.of(Duration.ofNanos(2)), DurationBasedTemporalUnit.ofNanos(2));

        assertNotEquals(DurationBasedTemporalUnit.of(Duration.ofHours(2)), DurationBasedTemporalUnit.ofMinutes(2));
    }

    @Test
    void testTruncate() {
        final var instant = Instant.parse("2024-06-25T13:00:00Z");
        final var instantOne = Instant.parse("2024-06-25T13:01:00Z");
        final var instantTwo = Instant.parse("2024-06-25T13:02:00Z");
        final var instantThree = Instant.parse("2024-06-25T13:03:00Z");
        final var instantFour = Instant.parse("2024-06-25T13:04:00Z");
        final var instantFive = Instant.parse("2024-06-25T13:05:00Z");
        assertEquals(instant, instantOne.truncatedTo(DurationBasedTemporalUnit.ofMinutes(5)));
        assertEquals(instant, instantTwo.truncatedTo(DurationBasedTemporalUnit.ofMinutes(5)));
        assertEquals(instant, instantThree.truncatedTo(DurationBasedTemporalUnit.ofMinutes(5)));
        assertEquals(instant, instantFour.truncatedTo(DurationBasedTemporalUnit.ofMinutes(5)));
        assertNotEquals(instant, instantFive.truncatedTo(DurationBasedTemporalUnit.ofMinutes(5)));
    }
}
