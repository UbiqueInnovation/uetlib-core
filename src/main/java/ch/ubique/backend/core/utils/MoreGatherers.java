package ch.ubique.backend.core.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Gatherer;

public final class MoreGatherers {
    private MoreGatherers() {}

    public static <T, K> Gatherer<T, Set<K>, T> distinctBy(final Function<T, K> f) {
        return Gatherer.ofSequential(
                HashSet::new,
                (map, obj, downstream) -> {
                    final var key = f.apply(obj);
                    if (!map.contains(key)) {
                        map.add(key);
                        downstream.push(obj);
                    }
                    return true;
                });
    }
}
