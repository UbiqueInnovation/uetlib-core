package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

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

    /**
     * Deep copies a map, creating a new map with the same keys and deep copies of the values.
     *
     * @param values the map to be deep copied
     * @param <K> the type of the keys in the map (must implement the Serializable interface)
     * @param <V> the type of the values in the map (must implement the Serializable interface)
     * @return a new map with the same keys and deep copies of the values
     */
    @NotNull
    public static <K extends Serializable, V extends Serializable> Map<K, V> deepCopy(
            @NotNull final Map<K, V> values) {
        return values.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> SerializationUtils.clone(entry.getKey()),
                                entry -> SerializationUtils.clone(entry.getValue())));
    }

    /**
     * Returns a DoubleStream consisting of the elements of the specified collection. Drops null
     * values.
     *
     * @param collection the collection of Double objects
     * @return a DoubleStream
     */
    public static DoubleStream doubleStream(final Collection<Double> collection) {
        return collection.stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue);
    }

    /**
     * Returns a List consisting of the elements of the specified collection. If the given
     * collection is already an instance of List, it is returned as is. Otherwise, a new List is
     * created using the elements of the given collection, in the same order. The returned List is
     * immutable.
     *
     * @param collection the collection of elements
     * @return a List containing the elements of the collection
     * @param <T> the type of elements in the collection
     */
    public static <T> List<T> asList(final Collection<T> collection) {
        if (collection instanceof final List<T> list) {
            return list;
        }
        return List.copyOf(collection);
    }

    /**
     * Returns a sorted List consisting of the elements of the specified collection. The elements of
     * the resulting List are sorted in their natural order as defined by the Comparable interface.
     *
     * @param collection the collection of elements to be sorted
     * @return a sorted List of elements
     * @param <T> the type of elements in the collection
     */
    public static <T extends Comparable<T>> List<T> asSortedList(final Collection<T> collection) {
        return collection.stream().sorted().toList();
    }
}
