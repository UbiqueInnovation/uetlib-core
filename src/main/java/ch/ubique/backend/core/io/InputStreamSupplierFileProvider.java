package ch.ubique.backend.core.io;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import com.pivovarit.function.ThrowingSupplier;

import org.apache.commons.io.FileUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

@NullMarked
public class InputStreamSupplierFileProvider implements FileProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ThrowingSupplier<InputStream, IOException> inputStreamSupplier;
    private final String filePrefix;
    private final String fileSuffix;

    @Nullable private Path filePath;

    /**
     * @see #InputStreamSupplierFileProvider(ThrowingSupplier, String, String)
     */
    public InputStreamSupplierFileProvider() {
        this(null, null, null);
    }

    /**
     * @see #InputStreamSupplierFileProvider(ThrowingSupplier, String, String)
     */
    public InputStreamSupplierFileProvider(
            @Nullable final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        this(inputStreamSupplier, null, null);
    }

    /**
     * Constructs an {@code InputStreamSupplierFileProvider} instance with an optional supplier for
     * providing the input stream and optional file naming configuration for temporary files.
     *
     * @param inputStreamSupplier a supplier for providing the input stream to be used, or {@code
     *     InputStream::nullInputStream} which returns a supplier of an empty input stream.
     * @param filePrefix an optional prefix for naming the temporary file. If {@code null}, a
     *     default prefix "file-provider-" will be used.
     * @param fileSuffix an optional suffix for naming the temporary file. If {@code null}, a
     *     default suffix ".tmp" will be used.
     */
    public InputStreamSupplierFileProvider(
            @Nullable final ThrowingSupplier<InputStream, IOException> inputStreamSupplier,
            @Nullable final String filePrefix,
            @Nullable final String fileSuffix) {
        this.inputStreamSupplier =
                requireNonNullElse(inputStreamSupplier, InputStream::nullInputStream);
        this.filePrefix = requireNonNullElse(filePrefix, "file-provider-");
        this.fileSuffix = requireNonNullElse(fileSuffix, ".tmp");
    }

    @Override
    @SuppressWarnings("java:S5443")
    public File file() throws IOException {
        if (filePath == null) {
            filePath = requireNonNull(Files.createTempFile(filePrefix, fileSuffix));
            logger.info("Saving input to temporary file {}.", filePath);
            try (final var inputStream = inputStreamSupplier.get()) {
                FileUtils.copyToFile(inputStream, filePath.toFile());
            }
            final var fileSize = FileUtils.byteCountToDisplaySize(Files.size(filePath));
            logger.info("Transferred {} from input stream to file", fileSize);
        }

        return filePath.toFile();
    }

    @Override
    public void close() throws IOException {
        try {
            if (filePath != null) {
                Files.delete(filePath);
            }
        } catch (final NoSuchFileException e) {
            /* Do nothing, no file to delete */
        }
    }
}
