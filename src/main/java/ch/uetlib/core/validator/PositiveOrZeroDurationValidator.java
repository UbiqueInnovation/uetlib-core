package ch.uetlib.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.PositiveOrZero;

import org.jspecify.annotations.Nullable;

import java.time.Duration;

/** Validates {@link PositiveOrZero} constraints on durations at nanosecond precision. */
public class PositiveOrZeroDurationValidator
        implements ConstraintValidator<PositiveOrZero, Duration> {

    @Override
    public boolean isValid(
            final @Nullable Duration value, final ConstraintValidatorContext context) {
        return value == null || !value.isNegative();
    }
}
