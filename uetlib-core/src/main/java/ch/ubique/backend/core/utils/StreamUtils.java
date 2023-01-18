package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.file.ByteFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

@SuppressWarnings("unused")
public class StreamUtils {

    private StreamUtils() {}

    public static InputStream openStreamFromUrl(final String url) throws IOException {
        return new URL(url).openStream();
    }

    public static <R> R consumeAndClose(
            final InputStream inputStream, final Function<InputStream, R> function)
            throws IOException {
        try (inputStream) {
            return function.apply(inputStream);
        }
    }

    public static ByteFile byteFileFromStream(
            final InputStream inputStream, final String fileName, final String contentType)
            throws IOException {
        try (inputStream) {
            final var bytes = inputStream.readAllBytes();
            return new ByteFile().setFileName(fileName).setBytes(bytes).setContentType(contentType);
        }
    }

    public static ZipArchiveInputStream zipStreamForEntry(
            final InputStream inputStream, final String zipEntryNameRegex) throws IOException {
        final var zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        findZipEntry(zipArchiveInputStream, zipEntryNameRegex);
        return zipArchiveInputStream;
    }

    public static ZipEntry findZipEntry(
            final ZipInputStream zipInputStream, final String zipEntryNameRegex)
            throws IOException {
        ZipEntry zipEntry;
        do {
            zipEntry = zipInputStream.getNextEntry();
        } while (zipEntry != null && !(zipEntry.getName()).matches(zipEntryNameRegex));
        return zipEntry;
    }

    public static ArchiveEntry findZipEntry(
            final ZipArchiveInputStream zipInputStream, final String name) throws IOException {
        ArchiveEntry zipEntry;
        do {
            zipEntry = zipInputStream.getNextEntry();
        } while (zipEntry != null && !(zipEntry.getName()).matches(name));
        return zipEntry;
    }

    /**
     * Creates a stream of chunks with lists of size {@param size}. The returned lists are created
     * lazily for each chunk, hence the stream is only consumed partially for each chunk and not all
     * at once to create the chunk lists.
     *
     * @param list the input list to be chunked
     * @param size size of the chunks
     * @return a stream of chunks
     * @param <T> The type of the list elements
     */
    public static <T> Stream<List<T>> chunked(final List<T> list, final int size) {
        final var iterator = list.stream().iterator();
        final var listIterator =
                new Iterator<List<T>>() {

                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public List<T> next() {
                        return IntStream.iterate(0, i -> i < size && iterator.hasNext(), i -> i + 1)
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
}
