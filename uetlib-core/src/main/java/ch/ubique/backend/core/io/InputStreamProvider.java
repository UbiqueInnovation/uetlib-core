package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.io.*;

public interface InputStreamProvider {
    static DirectInputStreamProvider of(final File file) throws FileNotFoundException {
        return new DirectInputStreamProvider(new FileInputStream(file));
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
}
