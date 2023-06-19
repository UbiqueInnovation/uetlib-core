package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotEmpty;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class CollectionUtils {
    private CollectionUtils() {}

    /**
     * Groups all items with the same value (according to valueExtractor).
     *
     * <p>Example: groupByValue(Set.of(1.2, 1.4, 1.9, 2.3), Math::round, 0) -> {1: [1.2, 1.4], 2:
     * [1.9, 2.3]}
     *
     * @param items collection of items to put in value classes
     * @param valueExtractor applied to every item. Value class is assigned based on evaluation of
     *     this extractor
     * @param epsilon the tolerance within which values are considered equal
     * @return A map with values as keys. For each key the value is a collection of items with that
     *     value (with epsilon tolerance).
     */
    public static <T> Map<Double, Collection<T>> groupByValue(
            final Collection<T> items,
            final ToDoubleFunction<T> valueExtractor,
            final double epsilon) {
        return valueClasses(items, valueExtractor, epsilon).stream()
                .collect(Collectors.toMap(c -> averageBy(c, valueExtractor), Function.identity()));
    }

    /**
     * Groups all items with the same value (according to valueExtractor).
     *
     * <p>Example: valueClasses(Set.of(1.2, 1.4, 1.9, 2.3), Math::round, 0) -> [[1.2, 1.4], [1.9,
     * 2.3]]
     *
     * @param items collection of items to put in value classes
     * @param valueExtractor applied to every item. Value class is assigned based on evaluation of
     *     this extractor
     * @param epsilon the tolerance within which values are considered equal
     * @return A collection of value classes. Each value class contains all items with the same
     *     value (with epsilon tolerance)
     */
    public static <T> Collection<Collection<T>> valueClasses(
            final Collection<T> items,
            final ToDoubleFunction<T> valueExtractor,
            final double epsilon) {
        final var valueClasses =
                new TreeSet<Collection<T>>(
                        Comparator.comparingDouble(c -> minBy(c, valueExtractor)));

        final var sortedItems =
                items.stream().sorted(Comparator.comparingDouble(valueExtractor)).toList();

        for (final var item : sortedItems) {
            valueClasses.stream()
                    .filter(list -> minDistance(valueExtractor, item, list) <= epsilon)
                    .findFirst()
                    .ifPresentOrElse(
                            v -> v.add(item),
                            () -> valueClasses.add(new ArrayList<>(List.of(item))));
        }
        return valueClasses;
    }

    public static <T> double minBy(
            @NotEmpty final Collection<T> collection, final ToDoubleFunction<T> valueExtractor) {
        return collection.stream().mapToDouble(valueExtractor).min().orElseThrow();
    }

    public static <T> double averageBy(
            @NotEmpty final Collection<T> collection, final ToDoubleFunction<T> valueExtractor) {
        return collection.stream().mapToDouble(valueExtractor).average().orElseThrow();
    }

    private static <T> double minDistance(
            final ToDoubleFunction<T> valueExtractor, final T item, final Collection<T> items) {
        return items.stream()
                .mapToDouble(element -> distance(valueExtractor, item, element))
                .min()
                .orElseThrow();
    }

    private static <T> double distance(
            final ToDoubleFunction<T> valueExtractor, final T a, final T b) {
        return Math.abs(valueExtractor.applyAsDouble(a) - valueExtractor.applyAsDouble(b));
    }
}
