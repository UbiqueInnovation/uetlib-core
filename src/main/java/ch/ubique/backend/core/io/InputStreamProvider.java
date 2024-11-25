package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.io.*;

public interface InputStreamProvider {

    static DirectFileProvider of(final File file) throws FileNotFoundException {
        return FileProvider.of(file);
    }

    static DirectInputStreamProvider of(final byte[] bytes) {
        return new DirectInputStreamProvider(new ByteArrayInputStream(bytes));
    }

    static DirectInputStreamProvider of(final InputStream inputStream) {
        return new DirectInputStreamProvider(inputStream);
    }

    static InputStreamSupplierProvider of(
        final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        return new InputStreamSupplierProvider(inputStreamSupplier);
    }

    InputStream inputStream() throws IOException;

    default FileProvider toFileProvider() {
        if (this instanceof final FileProvider fileProvider) {
            return fileProvider;
        }
        return FileProvider.of(this::inputStream);
    }
}
