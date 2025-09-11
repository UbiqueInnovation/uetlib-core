package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import jakarta.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;

public class InputStreamSupplierFileProvider implements FileProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ThrowingSupplier<InputStream, IOException> inputStreamSupplier;
    private final String filePrefix;
    private final String fileSuffix;
    private Path filePath;

    public InputStreamSupplierFileProvider(
            final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        this(inputStreamSupplier, null, null);
    }

    public InputStreamSupplierFileProvider(
            final ThrowingSupplier<InputStream, IOException> inputStreamSupplier,
            @Nullable final String filePrefix,
            @Nullable final String fileSuffix) {
        this.inputStreamSupplier = inputStreamSupplier;
        this.filePrefix = Objects.requireNonNullElse(filePrefix, "file-provider-");
        this.fileSuffix = Objects.requireNonNullElse(fileSuffix, ".tmp");
    }

    @Override
    @SuppressWarnings("java:S5443")
    public File file() throws IOException {
        if (filePath == null) {
            filePath = Files.createTempFile(filePrefix, fileSuffix);
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
