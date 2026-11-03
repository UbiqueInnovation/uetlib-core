package ch.ubique.uetlib.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Min;

import org.jspecify.annotations.Nullable;

import java.time.Year;

/** Validates {@link Min} constraints against the numeric year. */
public class MinYearValidator implements ConstraintValidator<Min, Year> {

    private long bound;

    @Override
    public void initialize(final Min constraint) {
        bound = constraint.value();
    }

    @Override
    public boolean isValid(final @Nullable Year value, final ConstraintValidatorContext context) {
        return value == null || value.getValue() >= bound;
    }
}
