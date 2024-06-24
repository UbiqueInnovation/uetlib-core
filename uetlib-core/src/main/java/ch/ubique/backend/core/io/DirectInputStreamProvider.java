package ch.ubique.backend.core.io;

import java.io.IOException;
import java.io.InputStream;

public class DirectInputStreamProvider implements InputStreamProvider {
    private final InputStream inputStream;

    public DirectInputStreamProvider(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public InputStream inputStream() throws IOException {
        return inputStream;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
