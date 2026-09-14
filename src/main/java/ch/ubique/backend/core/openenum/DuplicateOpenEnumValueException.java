package ch.ubique.backend.core.openenum;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Thrown when two known constants of an open enum share the same raw value, which would make
 * resolution ambiguous. Surfaced eagerly from {@link OpenEnumSet#of}.
 */
public class DuplicateOpenEnumValueException extends RuntimeException {

    public DuplicateOpenEnumValueException(final Class<? extends Enum<?>> knownType, final Object value) {
        super("Duplicate code value '%s' in %s".formatted(value, describe(knownType)));
    }

    private static String describe(final Class<?> knownType) {
        final var interfaces = knownType.getInterfaces();
        return interfaces.length == 0
                ? knownType.getName()
                : knownType.getName()
                        + " (implements "
                        + Arrays.stream(interfaces)
                                .map(Class::getSimpleName)
                                .collect(Collectors.joining(", "))
                        + ")";
    }
}
