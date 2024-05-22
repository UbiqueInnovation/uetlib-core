package ch.ubique.backend.core.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileProvider extends AutoCloseable {

    File file() throws IOException;

    @Override
    void close() throws IOException;

    default Path path() throws IOException {
        return file().toPath();
    }

    static DirectFileProvider of(final File file) {
        return new DirectFileProvider(file);
    }

    static AutoClosableTempFileProvider of(final byte[] bytes) {
        return new AutoClosableTempFileProvider(new ByteArrayInputStream(bytes));
    }

    static AutoClosableTempFileProvider of(final InputStream inputStream) {
        return new AutoClosableTempFileProvider(inputStream);
    }
}
