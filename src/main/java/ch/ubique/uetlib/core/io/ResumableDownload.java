package ch.ubique.uetlib.core.io;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Downloads that survive dropped connections by resuming via HTTP range requests. */
public final class ResumableDownload {

    private static final Logger logger = LoggerFactory.getLogger(ResumableDownload.class);

    private static final int CONNECT_TIMEOUT_MILLIS = 30_000;
    private static final int READ_TIMEOUT_MILLIS = 60_000;
    private static final int MAX_RETRIES_WITHOUT_PROGRESS = 5;

    private ResumableDownload() {}

    /**
     * Copies the content of the given URL to the given file. The destination file must not already
     * exist. Implementation follows <a href="https://www.baeldung.com/java-download-file">Baeldung:
     * Download a File From an URL in Java</a>.
     *
     * <p>If the connection drops mid-transfer, the download resumes from the last received byte
     * with an HTTP {@code Range} request instead of restarting from scratch. If the server does not
     * support range requests (plain {@code 200 OK} response), the download restarts from the
     * beginning on each retry instead. Gives up once {@value #MAX_RETRIES_WITHOUT_PROGRESS}
     * consecutive attempts have made no progress.
     *
     * @param url the URL to download from
     * @param file the destination file; must not already exist
     * @throws FileAlreadyExistsException if the destination file already exists
     * @throws IOException if the download did not complete
     */
    public static void copyURLToFile(final URL url, final File file) throws IOException {
        final var target = file.toPath();
        if (Files.exists(target)) {
            throw new FileAlreadyExistsException(target.toString());
        }
        for (var stalls = 0; stalls <= MAX_RETRIES_WITHOUT_PROGRESS; ) {
            final var have = Files.exists(target) ? Files.size(target) : 0L;
            try {
                final var connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
                connection.setReadTimeout(READ_TIMEOUT_MILLIS);
                connection.setRequestProperty("Range", "bytes=" + have + "-");
                final var status = connection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK
                        && status != HttpURLConnection.HTTP_PARTIAL) {
                    throw new IOException("HTTP " + status);
                }
                try (final var in = connection.getInputStream();
                        final var out =
                                Files.newOutputStream(
                                        target,
                                        StandardOpenOption.CREATE,
                                        status == HttpURLConnection.HTTP_PARTIAL
                                                ? StandardOpenOption.APPEND
                                                : StandardOpenOption.TRUNCATE_EXISTING)) {
                    in.transferTo(out);
                    return;
                }
            } catch (final IOException e) {
                stalls = Files.exists(target) && Files.size(target) > have ? 0 : stalls + 1;
                logger.info(
                        "Download of {} interrupted at byte {}, retrying",
                        urlWithoutQuery(url),
                        have,
                        e);
            }
        }
        throw new IOException(
                "Download of " + urlWithoutQuery(url) + " made no progress, giving up");
    }

    /** Signed URLs carry credentials in the query string; never log or throw with them. */
    private static String urlWithoutQuery(final URL url) {
        return url.getProtocol() + "://" + url.getAuthority() + url.getPath();
    }
}
