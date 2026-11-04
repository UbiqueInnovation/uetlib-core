package ch.uetlib.core.io;

import com.pivovarit.function.ThrowingSupplier;

import java.io.IOException;
import java.io.InputStream;

public record InputStreamSupplierProvider(
        ThrowingSupplier<InputStream, IOException> inputStreamThrowingSupplier)
        implements InputStreamProvider {

    @Override
    public InputStream inputStream() throws IOException {
        return inputStreamThrowingSupplier.get();
    }
}
