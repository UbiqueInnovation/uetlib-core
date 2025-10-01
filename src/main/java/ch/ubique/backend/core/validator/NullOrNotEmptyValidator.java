package ch.ubique.backend.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, Object> {

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        return switch (value) {
            // Valid if null
            case null -> true;

            // Check if it's a valid Collection or Map
            case final Collection<?> collection -> !collection.isEmpty();
            case final Map<?, ?> map -> !map.isEmpty();

            default -> {
                // Check if it's an array
                if (value.getClass().isArray()) {
                    // Valid if the array is not empty
                    if (Array.getLength(value) > 0) {
                        yield true;
                    }

                    // simple array.toString() produces "[Ljava.lang.Object;" garbage. Let us
                    // replace "validatedValue" in the default message template with an empty array,
                    // a more sensible toString ;)
                    final var messageTemplate =
                            context.getDefaultConstraintMessageTemplate()
                                    .replace("${validatedValue}", "[]");
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(messageTemplate)
                            .addConstraintViolation();
                    yield false;
                }

                // Invalid if it's not a Collection or Map or Array
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Only collections, maps, or arrays maps are allowed")
                        .addConstraintViolation();
                yield false;
            }
        };
    }
}
