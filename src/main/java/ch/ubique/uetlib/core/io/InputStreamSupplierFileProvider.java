package ch.ubique.uetlib.core.io;

import com.pivovarit.function.ThrowingSupplier;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputStreamSupplierFileProvider extends TempFileProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ThrowingSupplier<InputStream, IOException> inputStreamSupplier;

    /**
     * @see #InputStreamSupplierFileProvider(ThrowingSupplier, String, String)
     */
    InputStreamSupplierFileProvider(
            final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        this(inputStreamSupplier, null, null);
    }

    /**
     * Constructs an {@code InputStreamSupplierFileProvider} instance with a supplier for providing
     * the input stream and optional file naming configuration for temporary files.
     *
     * @param inputStreamSupplier a supplier for providing the input stream to be used
     * @param filePrefix see {@link TempFileProvider(String, String)}
     * @param fileSuffix see {@link TempFileProvider(String, String)}
     * @see TempFileProvider#TempFileProvider(String, String)
     */
    InputStreamSupplierFileProvider(
            final ThrowingSupplier<InputStream, IOException> inputStreamSupplier,
            final @Nullable String filePrefix,
            final @Nullable String fileSuffix) {
        super(filePrefix, fileSuffix);
        this.inputStreamSupplier = inputStreamSupplier;
    }

    @Override
    @SuppressWarnings("java:S5443")
    public File file() throws IOException {
        // create the file and write the input stream to it on first use
        if (filePath == null) {
            final var file = super.file();

            filePath = file.toPath();
            logger.info("Saving input to temporary file {}.", filePath);
            try (final var inputStream = inputStreamSupplier.get()) {
                FileUtils.copyToFile(inputStream, file);
            }
            final var fileSize = FileUtils.byteCountToDisplaySize(Files.size(filePath));
            logger.info("Transferred {} from input stream to file", fileSize);
        }

        return filePath.toFile();
    }
}
