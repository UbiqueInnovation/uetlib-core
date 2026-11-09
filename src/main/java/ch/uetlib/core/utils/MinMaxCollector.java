package ch.uetlib.core.utils;

import org.apache.commons.collections4.ComparatorUtils;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class MinMaxCollector {

    private MinMaxCollector() {}

    /**
     * @return MinMax<T> NotNull with {@link MinMax#min} and {@link MinMax#max} set
     * @throws NoSuchElementException if the stream did not contain any element
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, MinMax<T>> collect() {
        return collect(Comparator.<T>naturalOrder());
    }

    /**
     * @return MinMax<T> NotNull with {@link MinMax#min} and {@link MinMax#max} set
     * @throws NoSuchElementException if the stream did not contain any elements
     */
    public static <T> Collector<T, ?, MinMax<T>> collect(final Comparator<T> comparator) {
        return Collectors.collectingAndThen(
                collectMinMaxAccumulator(comparator), it -> it.minMaxMaybe().orElseThrow());
    }

    /**
     * @return Optional that is an {@link Optional#empty()} if the stream did not contain any
     *     elements
     */
    public static <T extends Comparable<? super T>>
            Collector<T, ?, Optional<MinMax<T>>> collectOptional() {
        return collectOptional(Comparator.<T>naturalOrder());
    }

    /**
     * @return Optional that is an {@link Optional#empty()} if the stream did not contain any
     *     elements
     */
    public static <T> Collector<T, ?, Optional<MinMax<T>>> collectOptional(
            final Comparator<T> comparator) {
        return Collectors.collectingAndThen(
                collectMinMaxAccumulator(comparator), MinMaxAccumulator::minMaxMaybe);
    }

    private static <T> Collector<T, ?, MinMaxAccumulator<T>> collectMinMaxAccumulator(
            final Comparator<T> comparator) {
        return Collectors.reducing(
                new EmptyAccumulator<>(), MinMaxAccumulator::of, (a, b) -> a.merge(b, comparator));
    }

    public record MinMax<T>(T min, T max) {}

    sealed interface MinMaxAccumulator<T> permits EmptyAccumulator, NonEmptyAccumulator {

        Optional<MinMax<T>> minMaxMaybe();

        MinMaxAccumulator<T> merge(MinMaxAccumulator<T> other, Comparator<T> comparator);

        static <T> MinMaxAccumulator<T> of(final @Nullable T value) {
            if (value == null) {
                return new EmptyAccumulator<>();
            }
            return new NonEmptyAccumulator<>(value, value);
        }
    }

    record EmptyAccumulator<T>() implements MinMaxAccumulator<T> {

        public Optional<MinMax<T>> minMaxMaybe() {
            return Optional.empty();
        }

        @Override
        public MinMaxAccumulator<T> merge(
                final MinMaxAccumulator<T> other, final Comparator<T> comparator) {
            return other;
        }
    }

    record NonEmptyAccumulator<T>(T min, T max) implements MinMaxAccumulator<T> {

        public Optional<MinMax<T>> minMaxMaybe() {
            return Optional.of(new MinMax<>(min, max));
        }

        @Override
        public MinMaxAccumulator<T> merge(
                final MinMaxAccumulator<T> other, final Comparator<T> comparator) {
            return switch (other) {
                case final EmptyAccumulator<T> ignored -> this;
                case NonEmptyAccumulator<T>(final var otherMin, final var otherMax) ->
                        new NonEmptyAccumulator<>(
                                ComparatorUtils.min(min, otherMin, comparator),
                                ComparatorUtils.max(max, otherMax, comparator));
            };
        }
    }
}
