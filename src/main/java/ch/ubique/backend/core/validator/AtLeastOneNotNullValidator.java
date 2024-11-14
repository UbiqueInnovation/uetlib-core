package ch.ubique.backend.core.validator;

import ch.ubique.backend.core.reflection.IntrospectionWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.util.Arrays;
import java.util.Objects;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {

    private String[] fields;

    @Override
    public void initialize(final AtLeastOneNotNull atLeastOneNotNull) {
        fields = atLeastOneNotNull.fields();
    }

    @Override
    public boolean isValid(
            final Object object, final ConstraintValidatorContext constraintValidatorContext) {

        final var beanWrapper = new IntrospectionWrapper(object);

        return Arrays.stream(fields).map(beanWrapper::getPropertyValue).anyMatch(Objects::nonNull);
    }
}
