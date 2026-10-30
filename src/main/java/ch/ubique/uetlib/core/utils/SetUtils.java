package ch.ubique.uetlib.core.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
}
