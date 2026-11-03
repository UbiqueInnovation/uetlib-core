package ch.ubique.uetlib.core.file.temp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TempFileManager implements AutoCloseable {

    private final List<Path> temporaryFileList = new ArrayList<>();

    /** Writes the given content in a temporary file and returns the path. */
    public Path createTempFile(final byte[] data, final String suffix) throws IOException {
        final var tmpFile = createTempPath(suffix);
        FileUtils.writeByteArrayToFile(tmpFile.toFile(), data);
        temporaryFileList.add(tmpFile);
        return tmpFile;
    }

    /** Creates a temporary file and returns the path. */
    public Path createEmptyTempFile(final String suffix) throws IOException {
        final var tmpFile = createTempPath(suffix);
        temporaryFileList.add(tmpFile);
        return tmpFile;
    }

    /** Writes the given content in a temporary file and returns the path. */
    public Path createTempFile(final InputStream in, final String suffix) throws IOException {
        final var tmpFile = createTempPath(suffix);
        try (final var stream = new FileOutputStream(tmpFile.toFile())) {
            IOUtils.copy(in, stream);
        }
        temporaryFileList.add(tmpFile);
        return tmpFile;
    }

    /** Creates a temporary file. */
    @SuppressWarnings("java:S5443")
    public Path createTempPath(final String suffix) throws IOException {
        final var tmp = Files.createTempFile(null, suffix);
        temporaryFileList.add(tmp);
        return tmp;
    }

    /** Deletes all temp files that have been created by this instance of the temp file manager. */
    public void deleteTempFiles() {
        temporaryFileList.forEach(path -> FileUtils.deleteQuietly(path.toFile()));
    }

    /** Get all temp files (Only required for testing). */
    public List<Path> getTempFiles() {
        return temporaryFileList;
    }

    /** Creates a temporary directory. */
    @SuppressWarnings("java:S5443")
    public Path createTempDir() throws IOException {
        final var tmp = Files.createTempDirectory(null);
        temporaryFileList.add(tmp);
        return tmp;
    }

    @Override
    public void close() {
        deleteTempFiles();
    }
}
