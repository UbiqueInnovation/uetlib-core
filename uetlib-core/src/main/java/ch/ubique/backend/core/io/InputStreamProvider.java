package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.io.*;

public interface InputStreamProvider extends AutoCloseable {
    static DirectInputStreamProvider of(final File file) throws FileNotFoundException {
        return new DirectInputStreamProvider(new FileInputStream(file));
    }

    static DirectInputStreamProvider of(final byte[] bytes) {
        return new DirectInputStreamProvider(new ByteArrayInputStream(bytes));
    }

    static DirectInputStreamProvider of(final InputStream inputStream) {
        return new DirectInputStreamProvider(inputStream);
    }

    static <E extends Exception> InputStreamSupplierInputStreamProvider<E> of(
            final ThrowingSupplier<InputStream, E> inputStreamSupplier) {
        return new InputStreamSupplierInputStreamProvider<>(inputStreamSupplier);
    }

    @SuppressWarnings("java:S112")
    InputStream inputStream() throws Exception;

    @Override
    void close() throws IOException;
}
