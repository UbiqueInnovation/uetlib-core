package ch.uetlib.core.validator;

import ch.uetlib.core.reflection.IntrospectionWrapper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExactlyOneNotNullValidator implements ConstraintValidator<ExactlyOneNotNull, Object> {

    private final List<String> fields = new ArrayList<>();

    @Override
    public void initialize(final ExactlyOneNotNull atLeastOneNotNull) {
        fields.clear();
        fields.addAll(Arrays.asList(atLeastOneNotNull.fields()));
    }

    @Override
    public boolean isValid(
            final Object object, final ConstraintValidatorContext constraintValidatorContext) {

        final var beanWrapper = new IntrospectionWrapper(object);

        final var nonNullCount =
                fields.stream().map(beanWrapper::getPropertyValue).filter(Objects::nonNull).count();
        return nonNullCount == 1;
    }
}
