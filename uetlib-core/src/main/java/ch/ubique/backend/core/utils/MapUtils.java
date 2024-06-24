package ch.ubique.backend.core.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MapUtils {
    private MapUtils() {}

    public static @NotNull <T, K extends Comparable<? super K>> List<T> sortMapByKeys(
            final Map<K, T> values) {
        return values.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
    }
}
