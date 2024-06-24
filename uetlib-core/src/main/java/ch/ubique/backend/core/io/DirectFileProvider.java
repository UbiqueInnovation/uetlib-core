package ch.ubique.backend.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public record DirectFileProvider(File file) implements FileProvider {

    @Override
    public InputStream inputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void close() {
        /* Nothing to do */
    }
}
