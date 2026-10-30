package ch.ubique.uetlib.core.utils;

import com.pivovarit.function.ThrowingSupplier;
import com.pivovarit.function.exception.WrappedException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

public final class OptionalUtils {

    private OptionalUtils() {}

    /**
     * Creates an {@link Optional} describing the given value, if non-null, otherwise returns an
     * empty {@link Optional}.
     *
     * <p>This method is a replacement for {@link Optional#ofNullable(Object)} to better support
     * JSpecify nullability annotations. Standard {@code Optional.ofNullable(@Nullable T)} often
     * results in {@code Optional<@Nullable T>}, whereas this method ensures {@code Optional<T>}.
     *
     * @param value the possibly null value
     * @param <T> the type of the value
     * @return an {@code Optional} with a present value if the specified value is non-null,
     *     otherwise an empty {@code Optional}
     */
    public static <T> Optional<T> ofNullable(final @Nullable T value) {
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    @SafeVarargs
    public static <T, E extends Exception> Optional<T> tryGet(
            final ThrowingSupplier<@Nullable T, E> supplier,
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
        return Stream.of(args).flatMap(Optional::stream).findFirst();
    }

    @SafeVarargs
    public static <T> Optional<T> coalesce(final @Nullable T... args) {
        return Stream.of(args).filter(Objects::nonNull).findFirst();
    }

    @SafeVarargs
    public static <T> Optional<T> coalesce(final Supplier<@Nullable T>... suppliers) {
        return Stream.of(suppliers).map(Supplier::get).filter(Objects::nonNull).findFirst();
    }

    public static <T> Stream<T> generateAsLongAsPresent(
            final Supplier<Optional<T>> optionalSupplier) {

        return Stream.generate(optionalSupplier)
                .takeWhile(Optional::isPresent)
                .map(Optional::orElseThrow);
    }
}
