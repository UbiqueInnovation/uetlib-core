package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.jetbrains.annotations.Contract;

public final class MinMaxCollector {

    private MinMaxCollector() {}

    /**
     * @return MinMax<T> NotNull with {@link MinMax#min} and {@link MinMax#max} set
     * @throws NoSuchElementException if the stream did not contain any element
     */
    public static <T extends Comparable<T>> Collector<T, ?, MinMax<T>> collect() {
        return collect(Comparator.<T>naturalOrder());
    }

    /**
     * @return MinMax<T> NotNull with {@link MinMax#min} and {@link MinMax#max} set
     * @throws NoSuchElementException if the stream did not contain any elements
     */
    public static <T extends Comparable<T>> Collector<T, ?, MinMax<T>> collect(
            @NotNull final Comparator<T> comparator) {
        return Collectors.collectingAndThen(
                collectMinMaxAccumulator(comparator),
                acc -> {
                    if (acc.min() == null || acc.max() == null) {
                        throw new NoSuchElementException();
                    }
                    return acc.toMinMax();
                });
    }

    /**
     * @return Optional that is an {@link Optional#empty()} if the stream did not contain any
     *     elements
     */
    public static <T extends Comparable<T>> Collector<T, ?, Optional<MinMax<T>>> collectOptional() {
        return collectOptional(Comparator.<T>naturalOrder());
    }

    /**
     * @return Optional that is an {@link Optional#empty()} if the stream did not contain any
     *     elements
     */
    public static <T extends Comparable<T>> Collector<T, ?, Optional<MinMax<T>>> collectOptional(
            @NotNull final Comparator<T> comparator) {
        return Collectors.collectingAndThen(
                collectMinMaxAccumulator(comparator),
                acc ->
                        acc.min() == null || acc.max() == null
                                ? Optional.empty()
                                : Optional.of(acc.toMinMax()));
    }

    private static <T extends Comparable<T>>
            Collector<T, ?, MinMaxAccumulator<T>> collectMinMaxAccumulator(
                    @NotNull final Comparator<T> comparator) {
        return Collectors.reducing(
                new MinMaxAccumulator<>(),
                a -> new MinMaxAccumulator<>(a, a),
                (a, b) ->
                        new MinMaxAccumulator<>(
                                minNullable(a.min(), b.min(), comparator),
                                maxNullable(a.max(), b.max(), comparator)));
    }

    @Nullable
    @Contract("null, _ -> param2; _, null -> param2; !null, !null -> !null")
    static <T extends Comparable<T>> T minNullable(@Nullable final T a, @Nullable final T b) {
        return minNullable(a, b, Comparator.naturalOrder());
    }

    @Nullable
    @Contract(
            "null, _, _ -> param2; _, null, _ -> param2; !null, !null, !null -> !null; !null, !null, null -> fail")
    static <T extends Comparable<T>> T minNullable(
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
    static <T extends Comparable<T>> T maxNullable(@Nullable final T a, @Nullable final T b) {
        return maxNullable(a, b, Comparator.naturalOrder());
    }

    @Nullable
    @Contract(
            "null, _, _ -> param2; _, null, _ -> param2; !null, !null, !null -> !null; !null, !null, null -> fail")
    static <T extends Comparable<T>> T maxNullable(
            @Nullable final T a, @Nullable final T b, @NotNull final Comparator<T> comparator) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return comparator.compare(a, b) > 0 ? a : b;
    }

    public record MinMax<T>(@NotNull T min, @NotNull T max) {}

    private record MinMaxAccumulator<T>(@Nullable T min, @Nullable T max) {

        MinMaxAccumulator() {
            this(null, null);
        }

        MinMax<T> toMinMax() {
            return new MinMax<>(min, max);
        }
    }
}
