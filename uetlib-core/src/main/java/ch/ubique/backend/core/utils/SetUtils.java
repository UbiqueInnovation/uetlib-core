package ch.ubique.backend.core.utils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SetUtils {

    private SetUtils() {}

    public static <T> Set<T> union(final Collection<T> a, final Collection<T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toUnmodifiableSet());
    }

    public static <T> Set<T> intersection(final Collection<T> a, final Collection<T> b) {
        return a.stream().filter(b::contains).collect(Collectors.toUnmodifiableSet());
    }

    public static <T> Set<T> exclude(final Collection<T> collection, final Collection<T> excluded) {
        return collection.stream()
                .filter(e -> !excluded.contains(e))
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T> Set<T> exclude(final Collection<T> collection, final T excluded) {
        return exclude(collection, Set.of(excluded));
    }
}
