package ch.ubique.uetlib.core.validator;

import ch.ubique.uetlib.core.reflection.IntrospectionWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {

    private final List<String> fields = new ArrayList<>();

    @Override
    public void initialize(final AtLeastOneNotNull atLeastOneNotNull) {
        fields.clear();
        fields.addAll(Arrays.asList(atLeastOneNotNull.fields()));
    }

    @Override
    public boolean isValid(
            final Object object, final ConstraintValidatorContext constraintValidatorContext) {

        final var beanWrapper = new IntrospectionWrapper(object);

        return fields.stream().map(beanWrapper::getPropertyValue).anyMatch(Objects::nonNull);
    }
}
