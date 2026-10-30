package ch.ubique.uetlib.core.io;

import java.io.File;

public record DirectFileProvider(File file) implements FileProvider {

    @Override
    public void close() {
        /* Nothing to do */
    }
}
