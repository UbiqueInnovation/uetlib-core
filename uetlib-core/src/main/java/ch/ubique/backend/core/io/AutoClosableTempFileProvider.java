package ch.ubique.backend.core.io;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AutoClosableTempFileProvider implements AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final InputStream inputStream;
    private Path filePath;

    public AutoClosableTempFileProvider(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public File getFile() throws IOException {
        return getFilePath().toFile();
    }

    public synchronized Path getFilePath() throws IOException {
        if (filePath == null) {
            filePath = Files.createTempFile(getClass().getSimpleName(), ".tmp");
            logger.info("Saving input to temporary file {}.", filePath);
            FileUtils.copyToFile(inputStream, filePath.toFile());
            final var fileSize = FileUtils.byteCountToDisplaySize(Files.size(filePath));
            logger.info("Transferred {} from input stream to file", fileSize);
        }
        return filePath;
    }

    @Override
    public void close() throws IOException {
        if (filePath != null) {
            Files.delete(filePath);
        }
    }
}
