package ch.ubique.uetlib.core.io;

import com.pivovarit.function.ThrowingSupplier;
import java.io.*;
import java.nio.file.Path;
import org.jspecify.annotations.Nullable;

/**
 * All temp files created by instances of {@link FileProvider} will be deleted on close. Users of
 * this class have to make sure that the {@link #close()} method is called when the file is no
 * longer needed., either directly or with try-with-resources.
 */
public interface FileProvider extends AutoCloseable, InputStreamProvider {

    /**
     * Creates a {@link FileProvider} that provides a temporary file. The file will be deleted on
     * close.
     *
     * @return a {@link FileProvider} that provides a temporary file
     */
    static FileProvider emptyTempFile() {
        return emptyTempFile(null, null);
    }

    /**
     * * Creates a {@link FileProvider} that provides a temporary file. The file will be deleted on
     * * close.
     *
     * @param filePrefix the prefix for the temporary file
     * @param fileSuffix the suffix for the temporary file
     * @return a {@link FileProvider} that provides a temporary file
     */
    static FileProvider emptyTempFile(
            final @Nullable String filePrefix, final @Nullable String fileSuffix) {
        return new TempFileProvider(filePrefix, fileSuffix);
    }

    /**
     * Creates a {@link FileProvider} that provides a file. The pre-existing file will not be
     * deleted on close.
     *
     * @param file the file to provide
     * @return a {@link FileProvider} that provides the data from the given file
     */
    static FileProvider of(final File file) {
        return new DirectFileProvider(file);
    }

    /**
     * Creates a {@link FileProvider} from the given bytes. The data is written to a temporary file
     * on calling {@link #inputStream()}
     *
     * @param bytes the bytes to provide
     * @return a {@link FileProvider} that provides the given bytes
     */
    static FileProvider of(final byte[] bytes) {
        return of(() -> new ByteArrayInputStream(bytes));
    }

    /**
     * Creates a {@link FileProvider} from the given {@link InputStream}. The data is written to a
     * temporary file on calling {@link #inputStream()}
     *
     * @param inputStream the input stream to provide
     * @return a {@link FileProvider} that provides the data of the input stream
     */
    static FileProvider of(final InputStream inputStream) {
        return of(() -> inputStream);
    }

    /**
     * Creates a {@link FileProvider} from the given {@link InputStream} supplier. The data is
     * written to a temporary file on calling {@link #inputStream()}
     *
     * @param inputStreamSupplier the supplier for the input stream
     * @return a {@link FileProvider} that provides the data of the input stream
     */
    static FileProvider of(final ThrowingSupplier<InputStream, IOException> inputStreamSupplier) {
        return of(inputStreamSupplier, null, null);
    }

    /**
     * Creates a {@link FileProvider} from the given {@link InputStream} supplier. The data is
     * written to a temporary file on calling {@link #inputStream()}
     *
     * @param inputStreamSupplier the supplier for the input stream
     * @param filePrefix the prefix for the temporary file
     * @param fileSuffix the suffix for the temporary file
     * @return a {@link FileProvider} that provides the data of the input stream
     */
    static FileProvider of(
            final ThrowingSupplier<InputStream, IOException> inputStreamSupplier,
            final @Nullable String filePrefix,
            final @Nullable String fileSuffix) {
        return new InputStreamSupplierFileProvider(inputStreamSupplier, filePrefix, fileSuffix);
    }

    File file() throws IOException;

    @Override
    void close() throws IOException;

    @Override
    default InputStream inputStream() throws IOException {
        return new FileInputStream(file());
    }

    default Path path() throws IOException {
        return file().toPath();
    }
}
