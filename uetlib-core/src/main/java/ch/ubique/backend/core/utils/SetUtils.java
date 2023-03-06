package ch.ubique.backend.core.utils;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SetUtils {

    private SetUtils() {}

    /**
     * Adapted from {@link org.apache.commons.collections4.SetUtils#difference} to support other
     * comparison than just using {@link Object#equals} by supplying a keyExtractor.
     *
     * <p>Use {@link org.apache.commons.collections4.SetUtils#difference} if your keyExtractor is
     * {@link Function#identity()}
     */
    public static <E, K> Set<E> difference(
            final Set<? extends E> a, final Set<? extends E> b, final Function<E, K> keyExtractor) {
        final var mappedRemoveKeys = b.stream().map(keyExtractor).toList();
        return a.stream()
                .filter(obj -> !mappedRemoveKeys.contains(keyExtractor.apply(obj)))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * @deprecated use {@link org.apache.commons.collections4.SetUtils#union}
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public static <T> Set<T> union(final Collection<T> a, final Collection<T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * @deprecated use {@link org.apache.commons.collections4.SetUtils#intersection}
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public static <T> Set<T> intersection(final Collection<T> a, final Collection<T> b) {
        return a.stream().filter(b::contains).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * @deprecated use {@link org.apache.commons.collections4.SetUtils#difference}
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public static <T> Set<T> exclude(final Collection<T> collection, final Collection<T> excluded) {
        return collection.stream()
                .filter(e -> !excluded.contains(e))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * @deprecated use {@link org.apache.commons.collections4.SetUtils#difference}
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public static <T> Set<T> exclude(final Collection<T> collection, final T excluded) {
        return exclude(collection, Set.of(excluded));
    }
}
