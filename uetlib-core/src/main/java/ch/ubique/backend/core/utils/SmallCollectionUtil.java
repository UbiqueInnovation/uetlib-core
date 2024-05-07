package ch.ubique.backend.core.utils;

import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class SmallCollectionUtil {

    private SmallCollectionUtil() {}

    public static <E> Collection<E> compress(final Stream<E> stream) {
        return compress(stream.toList());
    }

    public static <E> Collection<E> compress(final Collection<E> collection) {
        return switch (collection.size()) {
            case 0 -> List.of();
            case 1 -> singletonList(collection.stream().findAny().orElseThrow());
            default -> List.of(toArray(collection));
        };
    }

    public static <E> Collection<E> combine(final Collection<E> collection, final E additional) {
        return switch (collection.size()) {
            case 0 -> singletonList(additional);
            case 1 -> List.of(collection.stream().findAny().orElseThrow(), additional);
            default -> List.of(toArray(Stream.concat(collection.stream(), Stream.of(additional))));
        };
    }

    private static <E> E[] toArray(final Stream<E> stream) {
        //noinspection unchecked
        return (E[]) stream.toArray();
    }

    private static <E> E[] toArray(final Collection<E> collection) {
        //noinspection unchecked
        return (E[]) collection.toArray();
    }
}
