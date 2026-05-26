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

    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMap() {
        return Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue);
    }

    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMap(
                     final BinaryOperator<V> mergeFunction) {
        return Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue, mergeFunction);
    }

    public static <K, V, T extends Map.Entry<? extends K, ? extends V>, V2>
            Collector<T, ?, Map<K, V2>> entriesToMapWithValueMapper(
                     final Function<V, V2> valueMapper) {
        return Collectors.toUnmodifiableMap(
                Entry::getKey, entry -> valueMapper.apply(entry.getValue()));
    }

    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMapDropDuplicates() {
        return entriesToMap((a, b) -> a);
    }

    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMap() {
        return Collectors.toUnmodifiableMap(Entry::getValue, Entry::getKey);
    }

    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMap(
                     final BinaryOperator<K> mergeFunction) {
        return Collectors.toUnmodifiableMap(Entry::getValue, Entry::getKey, mergeFunction);
    }

    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMapDropDuplicates() {
        return entriesToInvertedMap((a, b) -> a);
    }

    public static <K, T> Collector<T, ?, Map<K, T>> toMap(
             final Function<T, K> keyExtractor) {
        return Collectors.toUnmodifiableMap(keyExtractor, identity());
    }

    public static <V, T> Collector<T, ?, Map<T, V>> toMapWithValueMapper(
             final Function<T, V> valueExtractor) {
        return Collectors.toUnmodifiableMap(identity(), valueExtractor);
    }

    /**
     * Given a stream of {@link Entry} elements, group them by the keys and map each group's
     * elements to the value of the entry
     *
     * @param <K> key type of the resulting map
     * @param <V> value type of the elements in the value list
     */
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
    public static <T, K, V> Collector<T, ?, Map<K, List<V>>> groupingByAndMapValues(
             final Function<T, K> keyExtractor,
             final Function<T, V> valueExtractor) {
        return Collectors.groupingBy(keyExtractor, mapping(valueExtractor, toList()));
    }
}
