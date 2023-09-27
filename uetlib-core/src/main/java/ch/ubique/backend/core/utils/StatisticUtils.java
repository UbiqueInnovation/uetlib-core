package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class provides utility methods for calculating statistics for a collection of numeric
 * values.
 */
public class StatisticUtils {
    private StatisticUtils() {}

    /**
     * Calculates statistics for a collection of numeric values.
     *
     * @param collection the collection of numeric values
     * @return the statistics for the collection
     * @throws NoSuchElementException if the collection is empty
     */
    public static NumericCollectionStats stats(final Collection<Double> collection) {
        return statsOptional(collection).orElseThrow();
    }

    /**
     * Calculates various statistical measures for a given collection of numeric values. Returns an
     * Optional containing the statistical measures if the collection is not null or empty,
     * otherwise returns an empty Optional.
     *
     * @param collection the collection of numeric values
     * @return an Optional containing the statistical measures if the collection is not null or
     *     empty, otherwise returns an empty Optional
     */
    public static Optional<NumericCollectionStats> statsOptional(
            final Collection<Double> collection) {
        if (collection == null || collection.isEmpty()) {
            return Optional.empty();
        }

        final var total = collection.size();

        final var median = median(collection).orElseThrow();
        final var percentiles = percentiles(collection, List.of(1, 5, 25, 75, 95, 99));

        final var doubleSummaryStats =
                collection.stream().collect(Collectors.summarizingDouble(Double::doubleValue));

        return Optional.of(
                new NumericCollectionStats(
                        total,
                        doubleSummaryStats.getMin(),
                        doubleSummaryStats.getMax(),
                        doubleSummaryStats.getAverage(),
                        median,
                        percentiles,
                        doubleSummaryStats.getSum()));
    }

    /**
     * Calculates and returns the median value of a collection of doubles.
     *
     * @param collection the collection of doubles
     * @return an {@code OptionalDouble} object containing the median value if the collection is not
     *     empty, otherwise an empty {@code OptionalDouble}
     */
    public static OptionalDouble median(final Collection<Double> collection) {
        final var sorted = CollectionUtils.asSortedList(collection);
        final var size = sorted.size();
        if (size % 2 == 0) {
            return CollectionUtils.doubleStream(sorted).skip((size / 2) - 1L).limit(2).average();
        }
        return CollectionUtils.doubleStream(sorted).skip(size / 2).findFirst();
    }

    /**
     * Calculates the specified percentiles for a given collection of numeric values.
     *
     * @param collection the collection of numeric values
     * @param percentiles the list of percentiles to calculate
     * @return a map containing the specified percentiles and their corresponding values
     */
    public static Map<Integer, Double> percentiles(
            final Collection<Double> collection, final Collection<Integer> percentiles) {
        final var sortedList = CollectionUtils.asSortedList(collection);
        return percentiles.stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                p -> percentileForSortedList(sortedList, p).orElseThrow()));
    }

    /**
     * Calculates and returns the specified percentile value of a collection of doubles.
     *
     * @param collection the collection of doubles
     * @param percentile the specified percentile value, must be between 0 and 99 (inclusive)
     * @return an {@code OptionalDouble} object containing the percentile value if the collection is
     *     not empty, otherwise an empty {@code OptionalDouble}
     * @throws IllegalArgumentException if percentile is not between 0 and 99 (inclusive)
     */
    public static OptionalDouble percentile(
            final Collection<Double> collection, @Min(0) @Max(99) final int percentile) {
        return percentileForSortedList(CollectionUtils.asSortedList(collection), percentile);
    }

    private static OptionalDouble percentileForSortedList(
            final List<Double> sortedList, @Min(0) @Max(99) final int percentile) {
        final var index = (int) Math.floor(percentile / 100.0 * sortedList.size());
        if (index < 0 || index >= sortedList.size()) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(sortedList.get(index));
    }
}
