package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MapCollectors {

    private MapCollectors() {}

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMap() {
        return Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMap(
                    @NotNull final BinaryOperator<V> mergeFunction) {
        return Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<K, V>> entriesToMapDropDuplicates() {
        return entriesToMap((a, b) -> a);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMap() {
        return Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMap(
                    @NotNull final BinaryOperator<K> mergeFunction) {
        return Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey, mergeFunction);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<? extends K, ? extends V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMapDropDuplicates() {
        return entriesToInvertedMap((a, b) -> a);
    }

    @NotNull
    public static <K, T> Collector<T, ?, Map<K, T>> toMap(
            @NotNull final Function<T, K> keyExtractor) {
        return Collectors.toUnmodifiableMap(keyExtractor, Function.identity());
    }

    @NotNull
    public static <V, T> Collector<T, ?, Map<T, V>> toMapWithValueMapper(
            @NotNull final Function<T, V> valueExtractor) {
        return Collectors.toUnmodifiableMap(Function.identity(), valueExtractor);
    }
}
