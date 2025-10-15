package ch.ubique.backend.core.validator;

import ch.ubique.backend.core.reflection.IntrospectionWrapper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Objects;

public class AtLeastOneNullValidator implements ConstraintValidator<AtLeastOneNull, Object> {

    private String[] fields;

    @Override
    public void initialize(final AtLeastOneNull atLeastOneNull) {
        fields = atLeastOneNull.fields();
    }

    @Override
    public boolean isValid(
            final Object object, final ConstraintValidatorContext constraintValidatorContext) {

        final var beanWrapper = new IntrospectionWrapper(object);

        return Arrays.stream(fields).map(beanWrapper::getPropertyValue).anyMatch(Objects::isNull);
    }
}
