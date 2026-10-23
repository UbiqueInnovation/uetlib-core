package ch.ubique.backend.core.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("DataFlowIssue")
class DurationValidatorTest {

    @Test
    void discoversValidatorsWithDefaultFactory() {
        try (final var factory = Validation.buildDefaultValidatorFactory()) {
            final var validator = factory.getValidator();
            assertInvalidProperties(validator, null, Set.of());
            assertInvalidProperties(validator, Duration.ZERO, Set.of("positive", "negative"));
            for (final var value :
                    new Duration[] {
                        Duration.ofNanos(1), Duration.ofSeconds(Long.MAX_VALUE, 999999999)
                    }) {
                assertInvalidProperties(validator, value, Set.of("negative", "negativeOrZero"));
            }
            for (final var value :
                    new Duration[] {Duration.ofNanos(-1), Duration.ofSeconds(Long.MIN_VALUE)}) {
                assertInvalidProperties(validator, value, Set.of("positive", "positiveOrZero"));
            }
            assertTrue(validator.validate(new Numbers(1, 0, -1, 0)).isEmpty());
            assertEquals(4, validator.validate(new Numbers(0, -1, 0, 1)).size());
        }
    }

    private void assertInvalidProperties(
            final Validator validator, @Nullable final Duration value, final Set<String> expected) {
        final var violations = validator.validate(new Durations(value, value, value, value));
        assertEquals(
                expected,
                violations.stream()
                        .map(v -> v.getPropertyPath().toString())
                        .collect(Collectors.toSet()));
    }

    record Durations(
            @Positive @Nullable Duration positive,
            @PositiveOrZero @Nullable Duration positiveOrZero,
            @Negative @Nullable Duration negative,
            @NegativeOrZero @Nullable Duration negativeOrZero) {}

    record Numbers(
            @Positive int positive,
            @PositiveOrZero int positiveOrZero,
            @Negative int negative,
            @NegativeOrZero int negativeOrZero) {}
}
