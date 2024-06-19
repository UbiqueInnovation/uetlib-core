package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.file.ByteFile;
import ch.ubique.backend.core.io.CompressionAlgorithm;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoInputStreamWrapper;
import com.j256.simplemagic.ContentInfoUtil;
import com.pivovarit.function.ThrowingSupplier;
import com.pivovarit.function.exception.WrappedException;

import jakarta.validation.constraints.NotNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("unused")
public final class InputStreamUtils {

    private static final Logger logger = LoggerFactory.getLogger(InputStreamUtils.class);

    private InputStreamUtils() {}

    public static InputStream openStreamFromUrl(final String url) throws IOException {
        return URI.create(url).toURL().openStream();
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

    public static InputStream uncompress(
            final InputStream inputStream, final String contentEncoding) throws IOException {
        return uncompress(inputStream, contentEncoding, null);
    }

    public static InputStream uncompress(
            final InputStream inputStream, final String contentEncoding, final String path)
            throws IOException {

        final var compressionAlgorithm =
                Stream.<ThrowingSupplier<CompressionAlgorithm, IOException>>of(
                                () -> findCompressionByContentInfo(inputStream),
                                () -> findCompressionByContentEncoding(contentEncoding),
                                () -> findCompressionByFilePath(path))
                        .map(ThrowingSupplier::sneaky)
                        .map(Supplier::get)
                        .filter(Objects::nonNull)
                        .findFirst();

        if (compressionAlgorithm.isEmpty()) {
            logger.debug("Could not find compression algorithm. Returning stream as-is");
            return inputStream;
        }

        return switch (compressionAlgorithm.get()) {
            case BZIP2 -> new BZip2CompressorInputStream(inputStream);
            case GZIP -> new GZIPInputStream(inputStream);
            case ZIP -> new ZipInputStream(inputStream);
            case COMPRESS_LZW -> new ZCompressorInputStream(inputStream);
            case DEFLATE -> new DeflateCompressorInputStream(inputStream);
            case BROTLI -> new BrotliCompressorInputStream(inputStream);
        };
    }

    private static CompressionAlgorithm findCompressionByContentInfo(final InputStream inputStream)
            throws IOException {
        final var contentInfo = contentInfo(inputStream);
        if (contentInfo == null) {
            logger.debug("Could not get content info from input stream.");
            return null;
        }
        final var contentType = contentInfo.getContentType();
        return switch (contentType) {
            case BZIP2 -> CompressionAlgorithm.BZIP2;
            case GZIP -> CompressionAlgorithm.GZIP;
            case ZIP -> CompressionAlgorithm.ZIP;
            default -> {
                logger.debug("No compression known for content type {}.", contentType);
                yield null;
            }
        };
    }

    public static CompressionAlgorithm findCompressionByContentEncoding(
            final String contentEncoding) {
        if (StringUtils.isBlank(contentEncoding)) {
            logger.debug("Content encoding is not set.");
            return null;
        }
        return switch (contentEncoding) {
            case "bzip2", "bz2" -> CompressionAlgorithm.BZIP2;
            case "gzip", "gz" -> CompressionAlgorithm.GZIP;
            case "zip" -> CompressionAlgorithm.ZIP;
            case "deflate" -> CompressionAlgorithm.DEFLATE;
            case "compress" -> CompressionAlgorithm.COMPRESS_LZW;
            case "br" -> CompressionAlgorithm.BROTLI;
            default -> {
                logger.info(
                        "content encoding {} not yet handled specifically, returning the stream"
                                + " as-is",
                        contentEncoding);
                yield null;
            }
        };
    }

    public static CompressionAlgorithm findCompressionByFilePath(final String filePath) {
        final var extension = FilenameUtils.getExtension(filePath);
        if (StringUtils.isBlank(extension)) {
            logger.debug("No file extension found for path {}", filePath);
            return null;
        }
        return switch (extension) {
            case "bzip2", "bz2" -> CompressionAlgorithm.BZIP2;
            case "gzip", "gz" -> CompressionAlgorithm.GZIP;
            case "zip" -> CompressionAlgorithm.ZIP;
            default -> {
                logger.debug(
                        "File extension {} does not indicate any known compression algorithm.",
                        filePath);
                yield null;
            }
        };
    }

    /**
     * Attempts to get {@link ContentInfo} from the given input stream and restoring the supplied
     * input stream after reading the first bytes to determine the content type
     */
    public static ContentInfo contentInfo(final InputStream inputStream) throws IOException {
        final var contentInfoInputStreamWrapper = new ContentInfoInputStreamWrapper(inputStream);

        if (!contentInfoInputStreamWrapper.markSupported()) {
            logger.debug(
                    "Cannot search the given input stream for its content type."
                            + " 'inputStream.markSupported()' returned false.");
            return null;
        }

        contentInfoInputStreamWrapper.mark(ContentInfoUtil.DEFAULT_READ_SIZE);

        final var match = contentInfoInputStreamWrapper.findMatch();
        // reset to the mark set above
        contentInfoInputStreamWrapper.reset();

        return match;
    }
}
