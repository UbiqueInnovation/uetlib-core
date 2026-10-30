package ch.ubique.uetlib.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Max;
import java.time.Year;
import org.jspecify.annotations.Nullable;

/** Validates {@link Max} constraints against the numeric year. */
public class MaxYearValidator implements ConstraintValidator<Max, Year> {

    private long bound;

    @Override
    public void initialize(final Max constraint) {
        bound = constraint.value();
    }

    @Override
    public boolean isValid(final @Nullable Year value, final ConstraintValidatorContext context) {
        return value == null || value.getValue() <= bound;
    }
}
