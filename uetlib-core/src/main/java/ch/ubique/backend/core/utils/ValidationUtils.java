package ch.ubique.backend.core.utils;

import jakarta.validation.*;

import java.util.Set;

public final class ValidationUtils {
    private static final Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    private ValidationUtils() {}

    public static boolean isValid(final Object object) {
        final var constraintViolations = validate(object);
        return constraintViolations.isEmpty();
    }

    public static <T> T requireIsValid(final T object) {
        final var constraintViolations = validate(object);
        if (constraintViolations.isEmpty()) {
            return object;
        }
        throw new ValidationException(
                "The provided object is not valid. Violations: " + constraintViolations);
    }

    public static <T> Set<ConstraintViolation<T>> validate(final T object) {
        return VALIDATOR.validate(object);
    }
}
