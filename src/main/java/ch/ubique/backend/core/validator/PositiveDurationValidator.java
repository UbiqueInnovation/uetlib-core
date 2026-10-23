package ch.ubique.backend.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Positive;

import org.jspecify.annotations.Nullable;

import java.time.Duration;

/** Validates {@link Positive} constraints on durations at nanosecond precision. */
public class PositiveDurationValidator implements ConstraintValidator<Positive, Duration> {

    @Override
    public boolean isValid(
            final @Nullable Duration value, final ConstraintValidatorContext context) {
        return value == null || (!value.isNegative() && !value.isZero());
    }
}
