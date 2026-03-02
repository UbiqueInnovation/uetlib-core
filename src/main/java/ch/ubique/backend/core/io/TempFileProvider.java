package ch.ubique.backend.core.io;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

@NullMarked
public class TempFileProvider implements FileProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String filePrefix;
    private final String fileSuffix;

    @Nullable Path filePath;

    /**
     * @see #TempFileProvider(String, String)
     */
    TempFileProvider() {
        this(null, null);
    }

    /**
     * Constructs a file provider that creates temporary files on use with optional pre- and suffix.
     *
     * @param filePrefix an optional prefix for naming the temporary file. If {@code null}, a
     *     default prefix "file-provider-" will be used.
     * @param fileSuffix an optional suffix for naming the temporary file. If {@code null}, a
     *     default suffix ".tmp" will be used.
     */
    TempFileProvider(@Nullable final String filePrefix, @Nullable final String fileSuffix) {
        this.filePrefix = requireNonNullElse(filePrefix, "file-provider-");
        this.fileSuffix = requireNonNullElse(fileSuffix, ".tmp");
    }

    @Override
    @SuppressWarnings("java:S5443")
    public File file() throws IOException {
        if (filePath == null) {
            filePath = requireNonNull(Files.createTempFile(filePrefix, fileSuffix));
            logger.info("Creating temporary file {}.", filePath);
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
