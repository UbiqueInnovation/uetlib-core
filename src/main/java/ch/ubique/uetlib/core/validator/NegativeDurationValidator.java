package ch.ubique.uetlib.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Negative;
import java.time.Duration;
import org.jspecify.annotations.Nullable;

/** Validates {@link Negative} constraints on durations at nanosecond precision. */
public class NegativeDurationValidator implements ConstraintValidator<Negative, Duration> {

    @Override
    public boolean isValid(
            final @Nullable Duration value, final ConstraintValidatorContext context) {
        return value == null || (value.isNegative());
    }
}
