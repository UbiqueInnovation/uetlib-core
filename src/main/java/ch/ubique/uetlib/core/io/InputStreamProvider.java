package ch.ubique.uetlib.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.io.*;

public interface InputStreamProvider {

    static FileProvider of(final File file) {
        return FileProvider.of(file);
    }

    static InputStreamProvider of(final byte[] bytes) {
        return new DirectInputStreamProvider(new ByteArrayInputStream(bytes));
    }

    static InputStreamProvider of(final InputStream inputStream) {
        return new DirectInputStreamProvider(inputStream);
    }

    static InputStreamProvider of(
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
