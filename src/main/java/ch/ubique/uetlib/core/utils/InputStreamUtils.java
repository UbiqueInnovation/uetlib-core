package ch.ubique.uetlib.core.utils;

import static java.util.Objects.requireNonNull;

import ch.ubique.uetlib.core.io.CompressionAlgorithm;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoInputStreamWrapper;
import com.j256.simplemagic.ContentInfoUtil;
import com.pivovarit.function.ThrowingSupplier;
import com.pivovarit.function.exception.WrappedException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
    public static BOMInputStream bomInputStream(final InputStream inputStream) {
        // asSupplier to wrap the IOException with UncheckedIOException
        return BOMInputStream.builder().setInputStream(inputStream).asSupplier().get();
    }

    public static BufferedReader bufferedReader(
            final InputStream inputStream, final Charset charset) {
        return new BufferedReader(new InputStreamReader(inputStream, charset));
    }

    public static BufferedReader bufferedReader(final InputStream inputStream) {
        return bufferedReader(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * Create a readable {@link InputStream} from a {@link ByteArrayOutputStream} by piping its
     * current contents into an {@link InputStream}.
     */
    @SuppressWarnings("java:S2095")
    public static InputStream pipeOutputToInputStream(final ByteArrayOutputStream originOut) {
        requireNonNull(originOut, "originOut");

        // connect the pipe
        final var inPipe = new PipedInputStream();

        try {
            //noinspection ExtractMethodRecommender
            final var outPipe = new PipedOutputStream(inPipe);
            final var writerThread =
                    new Thread(
                            () -> {
                                // write originOut into outPipe
                                try (outPipe) {
                                    originOut.writeTo(outPipe);
                                } catch (final IOException e) {
                                    throw new WrappedException(e);
                                }
                            },
                            "input-stream-utils-pipe-writer");
            writerThread.setDaemon(true);
            writerThread.start();
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
            final InputStream inputStream,
            final @Nullable String contentEncoding,
            final @Nullable String path)
            throws IOException {

        final var compressionAlgorithm =
                Stream.<ThrowingSupplier<@Nullable CompressionAlgorithm, IOException>>of(
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

    @Nullable
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

    @Nullable
    public static CompressionAlgorithm findCompressionByContentEncoding(
            final @Nullable String contentEncoding) {
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

    @Nullable
    public static CompressionAlgorithm findCompressionByFilePath(final @Nullable String filePath) {
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
    @Nullable
    public static ContentInfo contentInfo(final InputStream inputStream) throws IOException {
        final ContentInfoInputStreamWrapper contentInfoInputStreamWrapper;
        if (inputStream.markSupported()) {
            contentInfoInputStreamWrapper = new ContentInfoInputStreamWrapper(inputStream);
        } else {
            contentInfoInputStreamWrapper =
                    new ContentInfoInputStreamWrapper(
                            new BufferedInputStream(
                                    inputStream, ContentInfoUtil.DEFAULT_READ_SIZE));
        }

        contentInfoInputStreamWrapper.mark(ContentInfoUtil.DEFAULT_READ_SIZE);

        final var match = contentInfoInputStreamWrapper.findMatch();
        // reset to the mark set above
        contentInfoInputStreamWrapper.reset();

        return match;
    }

    public static HttpURLConnection connectWithRedirect(final URL url, final int timeout)
            throws IOException {
        final var urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(timeout);
        urlConnection.setReadTimeout(timeout);
        // Typically true by default, just making sure.
        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.connect();
        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_MOVED_TEMP
                && urlConnection.getResponseCode() != HttpURLConnection.HTTP_MOVED_PERM) {
            return urlConnection;
        }

        // Java HttpURLConnection never follows cross-protocol redirects (https -> http or vice
        // versa). Handle this explicitly.
        final var location = urlConnection.getHeaderField("Location");
        urlConnection.disconnect();
        final URL redirectedUrl;
        try {
            redirectedUrl = new URI(location).toURL();
        } catch (final URISyntaxException e) {
            throw new IOException("Invalid redirect location header", e);
        }
        logger.debug("Following redirect to {}", redirectedUrl);
        final var redirectedUrlConnection = (HttpURLConnection) redirectedUrl.openConnection();
        redirectedUrlConnection.setConnectTimeout(timeout);
        redirectedUrlConnection.setReadTimeout(timeout);
        redirectedUrlConnection.connect();
        return redirectedUrlConnection;
    }
}
