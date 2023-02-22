package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.file.ByteFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

@SuppressWarnings("unused")
public final class InputStreamUtils {

    private InputStreamUtils() {}

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
}
