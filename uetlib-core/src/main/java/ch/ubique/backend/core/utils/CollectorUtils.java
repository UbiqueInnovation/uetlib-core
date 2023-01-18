package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.jetbrains.annotations.Contract;

public class CollectorUtils {

    private CollectorUtils() {}

    /**
     * @return MinMax<T> NotNull with {@link MinMax#min} and {@link MinMax#max} set
     * @throws NoSuchElementException if the stream did not contain any element
     */
    @NotNull
    public static <T extends Comparable<T>> Collector<T, ?, MinMax<T>> collectMinMax() {
        return collectMinMax(Comparator.<T>naturalOrder());
    }

    /**
     * @return MinMax<T> NotNull with {@link MinMax#min} and {@link MinMax#max} set
     * @throws NoSuchElementException if the stream did not contain any elements
     */
    @NotNull
    public static <T extends Comparable<T>> Collector<T, ?, MinMax<T>> collectMinMax(
            @NotNull final Comparator<T> comparator) {
        return Collectors.collectingAndThen(
                collectMinMaxNullable(comparator),
                a -> {
                    if (a.min() == null || a.max() == null) {
                        throw new NoSuchElementException();
                    }
                    return a;
                });
    }

    /**
     * @return Optional that is an {@link Optional#empty()} if the stream did not contain any
     *     elements
     */
    @NotNull
    public static <T extends Comparable<T>>
            Collector<T, ?, Optional<MinMax<T>>> collectMinMaxOptional() {
        return collectMinMaxOptional(Comparator.<T>naturalOrder());
    }

    /**
     * @return Optional that is an {@link Optional#empty()} if the stream did not contain any
     *     elements
     */
    @NotNull
    public static <T extends Comparable<T>>
            Collector<T, ?, Optional<MinMax<T>>> collectMinMaxOptional(
                    @NotNull final Comparator<T> comparator) {
        return Collectors.collectingAndThen(
                collectMinMaxNullable(comparator),
                a -> a.min() == null || a.max() == null ? Optional.empty() : Optional.of(a));
    }

    /**
     * @return MinMax<T> with both {@link MinMax#min} and {@link MinMax#max} set or both null if the
     *     stream did not contain any elements
     * @throws NoSuchElementException if the stream did not contain any elements
     */
    @NotNull
    public static <T extends Comparable<T>> Collector<T, ?, MinMax<T>> collectMinMaxNullable() {
        return collectMinMaxNullable(Comparator.<T>naturalOrder());
    }

    /**
     * @return MinMax<T> with {@link MinMax#min} and {@link MinMax#max} both set to null if the
     *     stream did not contain any elements
     * @throws NoSuchElementException if the stream did not contain any elements
     */
    @NotNull
    public static <T extends Comparable<T>> Collector<T, ?, MinMax<T>> collectMinMaxNullable(
            @NotNull final Comparator<T> comparator) {
        return Collectors.reducing(
                new MinMax<>(),
                a -> new MinMax<>(a, a),
                (a, b) ->
                        new MinMax<>(
                                minNullable(a.min(), b.min(), comparator),
                                maxNullable(a.max(), b.max(), comparator)));
    }

    @NotNull
    @Contract("null, _ -> param2; _, null -> param2; !null, !null -> !null")
    public static <T extends Comparable<T>> T minNullable(
            @Nullable final T a, @Nullable final T b) {
        return minNullable(a, b, Comparator.naturalOrder());
    }

    @Nullable
    @Contract(
            "null, _, _ -> param2; _, null, _ -> param2; !null, !null, !null -> !null; !null, !null, null -> fail")
    public static <T extends Comparable<T>> T minNullable(
            @Nullable final T a, @Nullable final T b, @NotNull final Comparator<T> comparator) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return comparator.compare(a, b) <= 0 ? a : b;
    }

    @Nullable
    @Contract("null, _ -> param2; _, null -> param2; !null, !null -> !null")
    public static <T extends Comparable<T>> T maxNullable(
            @Nullable final T a, @Nullable final T b) {
        return maxNullable(a, b, Comparator.naturalOrder());
    }

    @Nullable
    @Contract(
            "null, _, _ -> param2; _, null, _ -> param2; !null, !null, !null -> !null; !null, !null, null -> fail")
    public static <T extends Comparable<T>> T maxNullable(
            @Nullable final T a, @Nullable final T b, @NotNull final Comparator<T> comparator) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return comparator.compare(a, b) > 0 ? a : b;
    }

    public record MinMax<T>(@Nullable T min, @Nullable T max) {

        public MinMax() {
            this(null, null);
        }
    }
}
