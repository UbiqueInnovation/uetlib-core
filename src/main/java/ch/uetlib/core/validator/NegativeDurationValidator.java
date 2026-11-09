package ch.uetlib.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Negative;

import org.jspecify.annotations.Nullable;

import java.time.Duration;

/** Validates {@link Negative} constraints on durations at nanosecond precision. */
public class NegativeDurationValidator implements ConstraintValidator<Negative, Duration> {

    @Override
    public boolean isValid(
            final @Nullable Duration value, final ConstraintValidatorContext context) {
        return value == null || (value.isNegative());
    }
}
