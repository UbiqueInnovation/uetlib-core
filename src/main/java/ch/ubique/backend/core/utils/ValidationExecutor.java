package ch.ubique.backend.core.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;

public class ValidationExecutor {

    private final Validator validator;

    public ValidationExecutor(final Validator validator) {
        this.validator = validator;
    }

    public <T> boolean isValid(final T object, final Class<?>... validationGroups) {
        return validate(object, validationGroups).isEmpty();
    }

    public <T> T requireIsValid(final T object, final Class<?>... validationGroups) {
        final var violations = validate(object, validationGroups);
        if (violations.isEmpty()) {
            return object;
        }
        throw new ValidationException(
                MessageFormat.format(
                        "The provided object is not valid for validation groups {0}. Violations:"
                                + " {1}",
                        Arrays.stream(validationGroups).map(Class::getSimpleName).toList(),
                        violations));
    }

    public <T> Set<ConstraintViolation<T>> validate(
            final T object, final Class<?>... validationGroups) {
        return validator.validate(object, validationGroups);
    }
}
