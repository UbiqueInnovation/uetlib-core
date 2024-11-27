package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.nio.file.NoSuchFileException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputStreamSupplierFileProvider implements FileProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ThrowingSupplier<InputStream, IOException> inputStreamSupplier;
    private Path filePath;

    public InputStreamSupplierFileProvider(
        final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        this.inputStreamSupplier = inputStreamSupplier;
    }

    @Override
    public File file() throws IOException {
        if (filePath == null) {
            filePath = Files.createTempFile(getClass().getSimpleName(), ".tmp");
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
