package ch.ubique.backend.core.utils;

import static java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class MapUtils {

    private MapUtils() {}

    public static @NotNull <T, K extends Comparable<? super K>> List<T> sortMapByKeys(
            final Map<K, T> values) {
        return values.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .map(Entry::getValue)
                .toList();
    }
}
