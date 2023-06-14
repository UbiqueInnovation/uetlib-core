package ch.ubique.backend.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class CollectionUtils {
    private CollectionUtils() {}

    public static <T> Map<Double, Collection<T>> groupByValue(
            final Collection<T> items,
            final ToDoubleFunction<T> valueExtractor,
            final double epsilon) {
        return valueClasses(items, valueExtractor, epsilon).stream()
                .collect(
                        Collectors.toMap(
                                collection ->
                                        collection.stream()
                                                .mapToDouble(valueExtractor)
                                                .average()
                                                .orElseThrow(),
                                Function.identity()));
    }

    public static <T> Collection<Collection<T>> valueClasses(
            final Collection<T> items,
            final ToDoubleFunction<T> valueExtractor,
            final double epsilon) {
        final var valueClasses = new HashSet<Collection<T>>();

        for (final var item : items) {
            final var valueClass =
                    valueClasses.stream()
                            .filter(list -> minDistance(valueExtractor, item, list) <= epsilon)
                            .findAny()
                            .orElseGet(
                                    () -> {
                                        final var newClass = new ArrayList<T>();
                                        valueClasses.add(newClass);
                                        return newClass;
                                    });
            valueClass.add(item);
        }
        return valueClasses;
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
