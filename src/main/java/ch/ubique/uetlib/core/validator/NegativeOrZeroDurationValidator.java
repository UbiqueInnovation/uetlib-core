package ch.ubique.uetlib.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NegativeOrZero;
import java.time.Duration;
import org.jspecify.annotations.Nullable;

/** Validates {@link NegativeOrZero} constraints on durations at nanosecond precision. */
public class NegativeOrZeroDurationValidator
        implements ConstraintValidator<NegativeOrZero, Duration> {

    @Override
    public boolean isValid(
            final @Nullable Duration value, final ConstraintValidatorContext context) {
        return value == null || (value.isNegative() || value.isZero());
    }
}
