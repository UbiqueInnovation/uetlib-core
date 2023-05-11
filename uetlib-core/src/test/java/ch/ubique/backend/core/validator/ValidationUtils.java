package ch.ubique.backend.core.validator;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Assertions;

public final class ValidationUtils {
    private ValidationUtils() {}

    private static ValidatorFactory validatorFactory() {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
    }

    public static void assertIsValid(final Object object) {
        try (final var validatorFactory = validatorFactory()) {
            final var validator = validatorFactory.getValidator();
            final var constraintViolations = validator.validate(object);
            Assertions.assertTrue(
                    constraintViolations.isEmpty(), "Object not valid: " + constraintViolations);
        }
    }

    public static void assertIsNotValid(final Object object) {
        try (final var validatorFactory = validatorFactory()) {
            final var validator = validatorFactory.getValidator();
            final var constraintViolations = validator.validate(object);
            Assertions.assertFalse(constraintViolations.isEmpty(), "Object is valid");
        }
    }
}
