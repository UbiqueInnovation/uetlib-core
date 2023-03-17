package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.mdc.MdcForkJoinPool;

import org.slf4j.MDC;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ListUtils {
    private ListUtils() {}

    /**
     * Adapted from {@link org.apache.commons.collections4.ListUtils#removeAll} to support other
     * comparison than just using {@link Object#equals} by supplying a keyExtractor.
     *
     * <p>Use {@link org.apache.commons.collections4.ListUtils#removeAll} if your keyExtractor is
     * {@link Function#identity()}
     */
    public static <E, K> List<E> removeAll(
            final Collection<E> collection,
            final Collection<E> remove,
            final Function<E, K> keyExtractor) {
        return collection.stream().filter(containsAnyByKey(remove, keyExtractor)).toList();
    }

    /**
     * Adapted from {@link org.apache.commons.collections4.ListUtils#removeAll} to support other
     * comparison than just using {@link Object#equals} by supplying a keyExtractor. Uses a
     * parallelStream.
     *
     * <p>Use {@link org.apache.commons.collections4.ListUtils#removeAll} if your keyExtractor is
     * {@link Function#identity()}
     */
    public static <E, K> List<E> removeAllParallel(
            final Collection<E> collection,
            final Collection<E> remove,
            final Function<E, K> keyExtractor) {
        final var mdcContext = MDC.getCopyOfContextMap();
        return MdcForkJoinPool.mdcCommonPool()
                .submit(
                        () ->
                                collection.parallelStream()
                                        .filter(containsAnyByKey(remove, keyExtractor))
                                        .toList(),
                        mdcContext)
                .join();
    }

    public static <E, K> Predicate<E> containsAnyByKey(
            final Collection<E> elements, final Function<E, K> keyExtractor) {
        final var mappedKeys =
                elements.stream().map(keyExtractor).collect(Collectors.toUnmodifiableSet());
        return element -> !mappedKeys.contains(keyExtractor.apply(element));
    }
}
