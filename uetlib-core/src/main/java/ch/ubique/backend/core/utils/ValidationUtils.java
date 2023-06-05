package ch.ubique.backend.core.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Set;

public final class ValidationUtils {
    private ValidationUtils() {}

    private static final ValidatorFactory VALIDATOR_FACTORY =
            Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory();

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
        final var validator = VALIDATOR_FACTORY.getValidator();
        return validator.validate(object);
    }
}
