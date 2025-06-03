package ch.ubique.backend.core.utils;

import static java.util.Map.Entry;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class MapCollectors {

    private MapCollectors() {}

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMap() {
        return Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMap(
                    @NotNull final BinaryOperator<V> mergeFunction) {
        return Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue, mergeFunction);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMapWithValueMapper(
                    @NotNull final Function<T, V> valueExtractor) {
        return Collectors.toUnmodifiableMap(Entry::getKey, valueExtractor);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMapDropDuplicates() {
        return entriesToMap((a, b) -> a);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMap() {
        return Collectors.toUnmodifiableMap(Entry::getValue, Entry::getKey);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMap(
                    @NotNull final BinaryOperator<K> mergeFunction) {
        return Collectors.toUnmodifiableMap(Entry::getValue, Entry::getKey, mergeFunction);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMapDropDuplicates() {
        return entriesToInvertedMap((a, b) -> a);
    }

    @NotNull
    public static <K, T> Collector<T, ?, Map<K, T>> toMap(
            @NotNull final Function<T, K> keyExtractor) {
        return Collectors.toUnmodifiableMap(keyExtractor, identity());
    }

    @NotNull
    public static <V, T> Collector<T, ?, Map<T, V>> toMapWithValueMapper(
            @NotNull final Function<T, V> valueExtractor) {
        return Collectors.toUnmodifiableMap(identity(), valueExtractor);
    }

    /**
     * Given a stream of {@link Entry} elements, group them by the keys and map each group's
     * elements to the value of the entry
     *
     * @param <K> key type of the resulting map
     * @param <V> value type of the elements in the value list
     */
    @NotNull
    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, List<V>>> groupingEntriesByKey() {
        return groupingByAndMapValues(Entry::getKey, Entry::getValue);
    }

    /**
     * Given a stream of elements, group them by the {@param keyExtractor} and map each group's
     * elements to the value resulting from the {@param valueExtractor}
     *
     * @param <T> element type of the stream to collect
     * @param <K> key type of the resulting map
     * @param <V> value type of the elements in the value list
     */
    @NotNull
    public static <T, K, V> Collector<T, ?, Map<K, List<V>>> groupingByAndMapValues(
            @NotNull final Function<T, K> keyExtractor,
            @NotNull final Function<T, V> valueExtractor) {
        return Collectors.groupingBy(keyExtractor, mapping(valueExtractor, toList()));
    }
}
