package ch.ubique.backend.core.file.temp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TempFileManager {

    private static final List<Path> temporaryFileList = new ArrayList<>();

    private TempFileManager() {}

    /**
     * Writes the given content in a temporary file and returns the path.
     *
     * @param data
     * @param suffix
     * @return
     * @throws IOException
     */
    public static Path createTempFile(final byte[] data, final String suffix) throws IOException {
        final var tmpFile = createTempPath(suffix);
        FileUtils.writeByteArrayToFile(tmpFile.toFile(), data);
        temporaryFileList.add(tmpFile);
        return tmpFile;
    }

    /**
     * Creates a temporary file and returns the path.
     *
     * @param suffix
     * @return
     * @throws IOException
     */
    public static Path createEmptyTempFile(final String suffix) throws IOException {
        final var tmpFile = createTempPath(suffix);
        temporaryFileList.add(tmpFile);
        return tmpFile;
    }

    /**
     * Writes the given content in a temporary file and returns the path.
     *
     * @param in
     * @param suffix
     * @return
     * @throws IOException
     */
    public static Path createTempFile(final InputStream in, final String suffix)
            throws IOException {
        final var tmpFile = createTempPath(suffix);
        try (final var stream = new FileOutputStream(tmpFile.toFile())) {
            IOUtils.copy(in, stream);
        }
        temporaryFileList.add(tmpFile);
        return tmpFile;
    }

    /**
     * Creates a temporary file.
     *
     * @return
     * @throws IOException
     */
    public static Path createTempPath(final String suffix) throws IOException {
        final var tmp = Files.createTempFile(null, suffix);
        temporaryFileList.add(tmp);
        return tmp;
    }

    /** Deletes all temp files that have been created by this instance of the temp file manager. */
    public static void deleteTempFiles() {
        temporaryFileList.forEach(path -> FileUtils.deleteQuietly(path.toFile()));
    }

    /** Get all temp files (Only required for testing). */
    public static List<Path> getTempFiles() {
        return temporaryFileList;
    }

    /**
     * Creates a temporary directory.
     *
     * @return
     * @throws IOException
     */
    public static Path createTempDir() throws IOException {
        final var tmp = Files.createTempDirectory(null);
        temporaryFileList.add(tmp);
        return tmp;
    }
}
