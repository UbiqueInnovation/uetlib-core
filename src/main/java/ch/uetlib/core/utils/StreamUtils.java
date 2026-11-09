package ch.uetlib.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.*;

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
    public static <T extends @Nullable Object> Stream<List<T>> chunked(
            final @Nullable List<T> list, final int chunkSize) {
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
    public static <T extends @Nullable Object> Stream<List<T>> chunked(
            final @Nullable Stream<T> stream, final int chunkSize) {
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
     * distinct to be a stateful filter, so use as a plugin to filter. DO NOT reuse the returned
     * predicate for multiple instances, as the internal map is captured by the returned Predicate.
     * Just use as `stream.filter(distinctByKey(Person::getName))`. Works with parallelStream.
     * Retains the first of each key seen.
     *
     * <p>See {@link Set#add(Object)} which returns a boolean if the object was already encountered.
     *
     * @param keyExtractor the extract the key
     * @param <T> the type of the elements
     * @param <K> the type of the key, non-null as {@link ConcurrentHashMap#putVal(Object, Object,
     *     boolean)} does not allow null keys
     * @return a predicate that returns if the key already exists
     */
    public static <T extends @Nullable Object, K> Predicate<T> distinctByKey(
            final Function<T, K> keyExtractor) {
        final Set<K> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /** Returns a {@link Stream#empty()} if {@code s == null || size <= 0 || size > s.length()} */
    public static <T extends @Nullable Object> Stream<List<T>> windows(
            final @Nullable List<T> list, final int size) {
        if (list == null || size <= 0 || size > list.size()) {
            return Stream.empty();
        }
        return IntStream.range(0, list.size() - size + 1)
                .mapToObj(start -> list.subList(start, start + size));
    }

    /**
     * Returns a {@link Stream<T>} containing the elements from {@code collection}, or an empty
     * {@link Stream<T>} if {@code collection} is empty or {@code null}
     */
    public static <T extends @Nullable Object> Stream<T> emptyIfNull(
            final @Nullable Collection<T> collection) {
        return CollectionUtils.emptyIfNull(collection).stream();
    }

    /**
     * Returns a {@link Stream<T>} containing the elements from {@code array}, or an empty {@link
     * Stream<T>} if {@code array} is empty or {@code null}
     */
    public static <T extends @Nullable Object> Stream<T> emptyIfNull(final T @Nullable [] array) {
        return array == null ? Stream.empty() : Arrays.stream(array);
    }

    /**
     * Returns a {@link IntStream} containing the elements from {@code array}, or an empty {@link
     * IntStream} if {@code array} is empty or {@code null}
     */
    public static IntStream emptyIfNull(final int @Nullable [] array) {
        return array == null ? IntStream.empty() : Arrays.stream(array);
    }

    /**
     * Returns a {@link LongStream} containing the elements from {@code array}, or an empty {@link
     * LongStream} if {@code array} is empty or {@code null}
     */
    public static LongStream emptyIfNull(final long @Nullable [] array) {
        return array == null ? LongStream.empty() : Arrays.stream(array);
    }

    /**
     * Returns a {@link DoubleStream} containing the elements from {@code array}, or an empty {@link
     * DoubleStream} if {@code array} is empty or {@code null}
     */
    public static DoubleStream emptyIfNull(final double @Nullable [] array) {
        return array == null ? DoubleStream.empty() : Arrays.stream(array);
    }
}
