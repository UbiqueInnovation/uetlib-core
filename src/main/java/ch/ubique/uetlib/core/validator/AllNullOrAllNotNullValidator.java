package ch.ubique.uetlib.core.validator;

import ch.ubique.uetlib.core.reflection.IntrospectionWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AllNullOrAllNotNullValidator
        implements ConstraintValidator<AllNullOrAllNotNull, Object> {

    private final List<String> fields = new ArrayList<>();

    @Override
    public void initialize(final AllNullOrAllNotNull allNullOrAllNotNull) {
        fields.clear();
        fields.addAll(Arrays.asList(allNullOrAllNotNull.fields()));
    }

    @Override
    public boolean isValid(
            final Object object, final ConstraintValidatorContext constraintValidatorContext) {

        final var beanWrapper = new IntrospectionWrapper(object);

        final var allNull =
                fields.stream().map(beanWrapper::getPropertyValue).allMatch(Objects::isNull);
        final var allNotNull =
                fields.stream().map(beanWrapper::getPropertyValue).allMatch(Objects::nonNull);

        return allNull || allNotNull;
    }
}
