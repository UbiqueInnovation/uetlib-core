package ch.uetlib.core.utils;

import static java.util.Map.Entry;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class MapUtils {

    private MapUtils() {}

    public static <T, K extends Comparable<? super K>> List<T> sortMapByKeys(
            final Map<K, T> values) {
        return values.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .map(Entry::getValue)
                .toList();
    }

    /**
     * Returns the provided {@link Entry} if both its key and value are non-null. If either the key
     * or the value is {@code null}, {@code null} is returned.
     *
     * @param entry the entry to check for null components
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @return the provided entry if both components are non-null, {@code null} otherwise
     */
    @SuppressWarnings("java:S1905")
    public static <K, V> @Nullable Entry<K, V> nonNullKeyValueOrNull(
            final Entry<@Nullable K, @Nullable V> entry) {
        if (entry.getKey() != null && entry.getValue() != null) {
            //noinspection RedundantCast
            return (Entry<K, V>) entry;
        }
        return null;
    }

    /**
     * Returns a new unmodifiable {@link Entry} if both the key and value are non-null. If either
     * the key or the value is {@code null}, {@code null} is returned.
     *
     * @param key the key to check
     * @param value the value to check
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @return a non-null unmodifiable entry if both components are non-null, {@code null} otherwise
     */
    public static <K, V> @Nullable Entry<K, V> nonNullKeyValueOrNull(
            final @Nullable K key, final @Nullable V value) {
        if (key == null || value == null) {
            return null;
        }
        //noinspection RedundantTypeArguments
        return Map.<K, V>entry(key, value);
    }
}
