package ch.ubique.backend.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/** The InputStream passed as an argument can only be consumed once. */
public class AutoClosableTempFileProvider implements FileProvider {

    private final InputStreamSupplierFileProvider fileProvider;

    public AutoClosableTempFileProvider(final InputStream inputStream) {
        this.fileProvider = new InputStreamSupplierFileProvider(() -> inputStream);
    }

    @Override
    public File file() throws IOException {
        return fileProvider.file();
    }

    public File getFile() throws IOException {
        return fileProvider.file();
    }

    public synchronized Path getFilePath() throws IOException {
        return fileProvider.path();
    }

    @Override
    public InputStream inputStream() throws IOException {
        return fileProvider.inputStream();
    }

    @Override
    public void close() throws IOException {
        fileProvider.close();
    }
}
