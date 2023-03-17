package ch.ubique.backend.core.utils;

import org.slf4j.MDC;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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
        final var mappedRemoveKeys = remove.stream().map(keyExtractor).toList();
        return collection.stream()
                .filter(obj -> !mappedRemoveKeys.contains(keyExtractor.apply(obj)))
                .toList();
    }

    /**
     * Adapted from {@link org.apache.commons.collections4.ListUtils#removeAll} to support other
     * comparison than just using {@link Object#equals} by supplying a keyExtractor. Uses a parallelStream.
     *
     * <p>Use {@link org.apache.commons.collections4.ListUtils#removeAll} if your keyExtractor is
     * {@link Function#identity()}
     */
    public static <E, K> List<E> removeAllParallel(
            final Collection<E> collection,
            final Collection<E> remove,
            final Function<E, K> keyExtractor) {
        final var mappedRemoveKeys = remove.stream().map(keyExtractor).toList();
        final var mdcContext = MDC.getCopyOfContextMap();
        return MdcUtils.mdcCommonPoolSubmitUnchecked(
                        () ->
                                collection.parallelStream()
                                        .filter(
                                                obj ->
                                                        !mappedRemoveKeys.contains(
                                                                keyExtractor.apply(obj)))
                                        .toList(),
                        mdcContext)
                .get();
    }
}
