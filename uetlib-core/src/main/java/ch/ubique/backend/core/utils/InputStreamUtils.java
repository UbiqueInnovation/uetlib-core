package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.file.ByteFile;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

        return new ByteFile()
                .setFileName(fileName)
                .setBytes(readBytesAndClose(inputStream))
                .setContentType(contentType);
    }

    public static byte[] readBytesAndClose(final InputStream inputStream) throws IOException {
        try (inputStream) {
            return inputStream.readAllBytes();
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
        final var pattern = Pattern.compile(zipEntryNameRegex);
        ZipEntry zipEntry;
        do {
            zipEntry = zipInputStream.getNextEntry();
        } while (zipEntry != null && !pattern.matcher(zipEntry.getName()).matches());
        return zipEntry;
    }

    public static ArchiveEntry findZipEntry(
            final ZipArchiveInputStream zipArchiveInputStream, final String archiveEntryNameRegex)
            throws IOException {
        final var pattern = Pattern.compile(archiveEntryNameRegex);
        ArchiveEntry archiveEntry;
        do {
            archiveEntry = zipArchiveInputStream.getNextEntry();
        } while (archiveEntry != null && !pattern.matcher(archiveEntry.getName()).matches());
        return archiveEntry;
    }
}
