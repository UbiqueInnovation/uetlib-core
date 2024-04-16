package ch.ubique.backend.core.utils;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MapCollectors {

    private MapCollectors() {}

    @NotNull
    public static <K, V, T extends Map.Entry<K, V>> Collector<T, ?, Map<K, V>> entriesToMap() {
        return Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<K, V>> Collector<T, ?, Map<K, V>> entriesToMap(
            @NotNull final BinaryOperator<V> mergeFunction) {
        return Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<K, V>>
            Collector<T, ?, Map<V, K>> entriesToInvertedMap() {
        return Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey);
    }

    @NotNull
    public static <K, V, T extends Map.Entry<K, V>> Collector<T, ?, Map<V, K>> entriesToInvertedMap(
            @NotNull final BinaryOperator<K> mergeFunction) {
        return Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey, mergeFunction);
    }
}
