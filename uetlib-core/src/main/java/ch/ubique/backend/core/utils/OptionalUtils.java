package ch.ubique.backend.core.utils;

import com.pivovarit.function.ThrowingSupplier;
import com.pivovarit.function.exception.WrappedException;

import java.util.Arrays;
import java.util.Optional;

public class OptionalUtils {
    private OptionalUtils() {}

    @SafeVarargs
    public static <T, E extends Exception> Optional<T> tryGet(
            final ThrowingSupplier<T, E> supplier,
            final Class<? extends Exception>... allowedExceptions) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (final Exception e) {
            if (allowedExceptions.length == 0
                    || Arrays.stream(allowedExceptions)
                            .anyMatch(allowed -> allowed.isAssignableFrom(e.getClass()))) {
                return Optional.empty();
            }
            if (e instanceof final RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new WrappedException(e);
        }
    }

    @SafeVarargs
    public static <T> Optional<T> coalesce(final Optional<T>... args) {
        for (final var arg : args) {
            if (arg.isPresent()) {
                return arg;
            }
        }
        return Optional.empty();
    }

    @SafeVarargs
    public static <T> Optional<T> coalesce(final T... args) {
        for (final var arg : args) {
            if (arg != null) {
                return Optional.of(arg);
            }
        }
        return Optional.empty();
    }
}
