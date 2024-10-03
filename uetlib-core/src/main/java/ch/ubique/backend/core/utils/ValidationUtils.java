package ch.ubique.backend.core.utils;

import jakarta.validation.*;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;

public final class ValidationUtils {

    private static final Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    private ValidationUtils() {}

    public static <T> boolean isValid(final T object, final Class<?>... validationGroups) {
        final var constraintViolations = validate(object, validationGroups);
        return constraintViolations.isEmpty();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <T> T requireIsValid(final T object, final Class<?>... validationGroups) {
        final var constraintViolations = validate(object, validationGroups);
        if (constraintViolations.isEmpty()) {
            return object;
        }
        throw new ValidationException(
                MessageFormat.format(
                        "The provided object is not valid for validation groups {0}. Violations: {1}",
                        Arrays.stream(validationGroups).map(Class::getSimpleName).toList(),
                        constraintViolations));
    }

    public static <T> Set<ConstraintViolation<T>> validate(
            final T object, final Class<?>... validationGroups) {
        return VALIDATOR.validate(object, validationGroups);
    }
}
