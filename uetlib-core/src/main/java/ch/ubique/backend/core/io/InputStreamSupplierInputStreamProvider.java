package ch.ubique.backend.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.io.IOException;
import java.io.InputStream;

public record InputStreamSupplierInputStreamProvider<E extends Exception>(
        ThrowingSupplier<InputStream, E> inputStreamThrowingSupplier)
        implements InputStreamProvider {

    @Override
    public InputStream inputStream() throws E {
        return inputStreamThrowingSupplier.get();
    }

    @Override
    public void close() throws IOException {}
}
