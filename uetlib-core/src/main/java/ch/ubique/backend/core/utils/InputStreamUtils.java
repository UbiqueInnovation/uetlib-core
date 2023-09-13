package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.file.ByteFile;

import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentType;
import com.pivovarit.function.exception.WrappedException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.input.BOMInputStream;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("unused")
public final class InputStreamUtils {

    private static final Logger logger = LoggerFactory.getLogger(InputStreamUtils.class);
    private static final ContentInfoUtil CONTENT_INFO_UTIL = new ContentInfoUtil();

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

    /**
     * Wrap an input stream in a {@link BOMInputStream} if the file starts with a BOM that needs to
     * be stripped before parsing.
     */
    @NotNull
    public static BOMInputStream bomInputStream(final InputStream inputStream) {
        // asSupplier to wrap the IOException with UncheckedIOException
        return BOMInputStream.builder().setInputStream(inputStream).asSupplier().get();
    }

    @NotNull
    public static BufferedReader bufferedReader(
            final InputStream inputStream, final Charset charset) {
        return new BufferedReader(new InputStreamReader(inputStream, charset));
    }

    @NotNull
    public static BufferedReader bufferedReader(final InputStream inputStream) {
        return bufferedReader(inputStream, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("java:S2095")
    public static InputStream pipeOutputToInputStream(final ByteArrayOutputStream originOut) {

        // connect the pipe
        final var inPipe = new PipedInputStream();

        try {
            final var outPipe = new PipedOutputStream(inPipe);
            new Thread(
                            () -> {
                                // write originOut into outPipe
                                try (outPipe) {
                                    originOut.writeTo(outPipe);
                                } catch (final IOException e) {
                                    throw new WrappedException(e);
                                }
                            })
                    .start();
            // return the input stream
            return inPipe;
        } catch (final IOException e) {
            throw new WrappedException(e);
        }
    }

    public static InputStream uncompress(final InputStream inputStream) throws IOException {
        final var contentType = contentType(inputStream);
        return switch (contentType) {
            case BZIP2 -> new BZip2CompressorInputStream(inputStream);
            case GZIP -> new GZIPInputStream(inputStream);
            case ZIP -> new ZipInputStream(inputStream);
            default -> {
                logger.info(
                        "content type {} not yet handled specifically, returning the stream as-is.",
                        contentType);
                yield inputStream;
            }
        };
    }

    public static ContentType contentType(final InputStream inputStream) throws IOException {
        return CONTENT_INFO_UTIL.findMatch(inputStream).getContentType();
    }
}
