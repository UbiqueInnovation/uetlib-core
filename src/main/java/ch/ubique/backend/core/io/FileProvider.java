package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import jakarta.annotation.Nullable;

import java.io.*;
import java.nio.file.Path;

public interface FileProvider extends AutoCloseable, InputStreamProvider {

    static FileProvider of(final File file) {
        return new DirectFileProvider(file);
    }

    static FileProvider of(final byte[] bytes) {
        return of(new ByteArrayInputStream(bytes));
    }

    static FileProvider of(final InputStream inputStream) {
        return new AutoClosableTempFileProvider(inputStream);
    }

    static FileProvider of(final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        return of(inputStreamSupplier, null, null);
    }

    static FileProvider of(
            final ThrowingSupplier<InputStream, IOException> inputStreamSupplier,
            @Nullable final String filePrefix,
            @Nullable final String fileSuffix) {
        return new InputStreamSupplierFileProvider(inputStreamSupplier, filePrefix, fileSuffix);
    }

    File file() throws IOException;

    @Override
    void close() throws IOException;

    @Override
    default InputStream inputStream() throws IOException {
        return new FileInputStream(file());
    }

    default Path path() throws IOException {
        return file().toPath();
    }
}
