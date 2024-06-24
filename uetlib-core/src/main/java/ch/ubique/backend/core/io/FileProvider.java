package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileProvider extends AutoCloseable, InputStreamProvider {

    static DirectFileProvider of(final File file) {
        return new DirectFileProvider(file);
    }

    static AutoClosableTempFileProvider of(final byte[] bytes) {
        return of(new ByteArrayInputStream(bytes));
    }

    static AutoClosableTempFileProvider of(final InputStream inputStream) {
        return new AutoClosableTempFileProvider(inputStream);
    }

    static InputStreamSupplierFileProvider of(
            final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        return new InputStreamSupplierFileProvider(inputStreamSupplier);
    }

    File file() throws IOException;

    @Override
    void close() throws IOException;

    default Path path() throws IOException {
        return file().toPath();
    }
}
