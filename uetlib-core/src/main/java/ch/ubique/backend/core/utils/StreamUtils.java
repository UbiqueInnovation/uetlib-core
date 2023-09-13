package ch.ubique.backend.core.utils;


import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
public final class StreamUtils {

    private StreamUtils() {}

    /**
     * Creates a stream of chunks with lists of size {@param size}. The returned lists are created
     * lazily for each chunk, hence the stream is only consumed partially for each chunk and not all
     * at once to create the chunk lists.
     *
     * @param list the input list to be chunked
     * @param chunkSize size of the chunks
     * @return a stream of chunks
     * @param <T> The type of the list elements
     */
    public static <T> Stream<List<T>> chunked(final List<T> list, final int chunkSize) {
        if (list == null || chunkSize <= 0) {
            return Stream.empty();
        }
        return chunked(list.stream(), chunkSize);
    }

    /**
     * Creates a stream of chunks with lists of size {@param size}. The returned lists are created
     * lazily for each chunk, hence the stream is only consumed partially for each chunk and not all
     * at once to create the chunk lists.
     *
     * @param stream the input stream to be chunked
     * @param chunkSize size of the chunks
     * @return a stream of chunks
     * @param <T> The type of the stream elements
     */
    public static <T> Stream<List<T>> chunked(final Stream<T> stream, final int chunkSize) {
        if (stream == null || chunkSize <= 0) {
            return Stream.empty();
        }
        final var iterator = stream.iterator();
        final var listIterator =
                new Iterator<List<T>>() {

                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public List<T> next() {
                        return IntStream.iterate(
                                        0, i -> i < chunkSize && iterator.hasNext(), i -> i + 1)
                                .mapToObj(i -> iterator.next())
                                .toList();
                    }
                };
        return StreamSupport.stream(((Iterable<List<T>>) () -> listIterator).spliterator(), false);
    }

    /**
     * A replacement for Stream::distinct to compare on a key of the streamed elements. Consider
     * distinct to be a stateful filter, so use as plugin to filter. DO NOT reuse the returned
     * predicate for multiple instances, as the internal map is captured by the returned Predicate.
     * Just use as `stream.filter(distinctByKey(Person::getName))`. Works with parallelStream.
     * Retains the first of each key seen.
     *
     * @param keyExtractor the extract the key
     * @param <T> the type of the elements
     * @param <K> the type of the key
     * @return a predicate that returns if the key already exists
     */
    public static <T, K> Predicate<T> distinctByKey(final Function<T, K> keyExtractor) {
        final Set<K> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /** Returns a {@link Stream#empty()} if {@code s == null || size <= 0 || size > s.length()} */
    public static <T> Stream<List<T>> windows(final List<T> list, final int size) {
        if (list == null || size <= 0 || size > list.size()) {
            return Stream.empty();
        }
        return IntStream.range(0, list.size() - size + 1)
                .mapToObj(start -> list.subList(start, start + size));
    }

    /**
     * Returns a {@link Stream<T>} containing the elements from {@code collection}, or an empty
     * {@link Stream<T>} none if {@code collection} is empty or {@code null}
     */
    public static <T> Stream<T> emptyIfNull(final Collection<T> collection) {
        return CollectionUtils.emptyIfNull(collection).stream();
    }
}
