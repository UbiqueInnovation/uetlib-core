package ch.ubique.backend.core.utils.duraiton;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ch.ubique.backend.core.utils.duration.DurationUnit;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

class DurationUnitTest {
    @Test
    void testOf() {
        assertEquals(DurationUnit.of(Duration.ofDays(2)), DurationUnit.ofDays(2));
        assertEquals(DurationUnit.of(Duration.ofHours(2)), DurationUnit.ofHours(2));
        assertEquals(DurationUnit.of(Duration.ofMinutes(2)), DurationUnit.ofMinutes(2));
        assertEquals(DurationUnit.of(Duration.ofSeconds(2)), DurationUnit.ofSeconds(2));
        assertEquals(DurationUnit.of(Duration.ofMillis(2)), DurationUnit.ofMillis(2));
        assertEquals(DurationUnit.of(Duration.ofNanos(2)), DurationUnit.ofNanos(2));

        assertNotEquals(DurationUnit.of(Duration.ofHours(2)), DurationUnit.ofMinutes(2));
    }

    @Test
    void testTruncate() {
        final var instant = Instant.parse("2024-06-25T13:00:00Z");
        final var instantOne = Instant.parse("2024-06-25T13:01:00Z");
        final var instantTwo = Instant.parse("2024-06-25T13:02:00Z");
        final var instantThree = Instant.parse("2024-06-25T13:03:00Z");
        final var instantFour = Instant.parse("2024-06-25T13:04:00Z");
        final var instantFive = Instant.parse("2024-06-25T13:05:00Z");
        assertEquals(instant, instantOne.truncatedTo(DurationUnit.ofMinutes(5)));
        assertEquals(instant, instantTwo.truncatedTo(DurationUnit.ofMinutes(5)));
        assertEquals(instant, instantThree.truncatedTo(DurationUnit.ofMinutes(5)));
        assertEquals(instant, instantFour.truncatedTo(DurationUnit.ofMinutes(5)));
        assertNotEquals(instant, instantFive.truncatedTo(DurationUnit.ofMinutes(5)));
    }
}
