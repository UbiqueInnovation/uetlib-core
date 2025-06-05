package ch.ubique.backend.core.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.util.Set;

public final class ValidationUtils {

    private static final ValidationExecutor DEFAULT_EXECUTOR =
            new ValidationExecutor(Validation.buildDefaultValidatorFactory().getValidator());

    private ValidationUtils() {}

    public static <T> boolean isValid(final T object, final Class<?>... validationGroups) {
        return DEFAULT_EXECUTOR.isValid(object, validationGroups);
    }

    public static <T> T requireIsValid(final T object, final Class<?>... validationGroups) {
        return DEFAULT_EXECUTOR.requireIsValid(object, validationGroups);
    }

    public static <T> Set<ConstraintViolation<T>> validate(
            final T object, final Class<?>... validationGroups) {
        return DEFAULT_EXECUTOR.validate(object, validationGroups);
    }
}
