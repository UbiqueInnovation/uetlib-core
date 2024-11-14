package ch.ubique.backend.core.validator;

import ch.ubique.backend.core.reflection.IntrospectionWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.util.Arrays;
import java.util.Objects;

public class AllNullOrAllNotNullValidator
        implements ConstraintValidator<AllNullOrAllNotNull, Object> {

    private String[] fields;

    @Override
    public void initialize(final AllNullOrAllNotNull allNullOrAllNotNull) {
        fields = allNullOrAllNotNull.fields();
    }

    @Override
    public boolean isValid(
            final Object object, final ConstraintValidatorContext constraintValidatorContext) {

        final var beanWrapper = new IntrospectionWrapper(object);

        final var allNull =
                Arrays.stream(fields).map(beanWrapper::getPropertyValue).allMatch(Objects::isNull);
        final var allNotNull =
                Arrays.stream(fields).map(beanWrapper::getPropertyValue).allMatch(Objects::nonNull);

        return allNull || allNotNull;
    }
}
