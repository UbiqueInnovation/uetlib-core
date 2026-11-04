package ch.uetlib.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, CharSequence> {

    @Override
    public boolean isValid(
            final @Nullable CharSequence s,
            final ConstraintValidatorContext constraintValidatorContext) {
        return s == null || StringUtils.isNotBlank(s);
    }
}
