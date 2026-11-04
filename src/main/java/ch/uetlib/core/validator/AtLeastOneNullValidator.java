package ch.uetlib.core.validator;

import ch.uetlib.core.reflection.IntrospectionWrapper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AtLeastOneNullValidator implements ConstraintValidator<AtLeastOneNull, Object> {

    private final List<String> fields = new ArrayList<>();

    @Override
    public void initialize(final AtLeastOneNull atLeastOneNull) {
        fields.clear();
        fields.addAll(Arrays.asList(atLeastOneNull.fields()));
    }

    @Override
    public boolean isValid(
            final Object object, final ConstraintValidatorContext constraintValidatorContext) {

        final var beanWrapper = new IntrospectionWrapper(object);

        return fields.stream().map(beanWrapper::getPropertyValue).anyMatch(Objects::isNull);
    }
}
