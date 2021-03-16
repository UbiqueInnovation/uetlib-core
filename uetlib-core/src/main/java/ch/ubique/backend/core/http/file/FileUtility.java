package ch.ubique.backend.core.http.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class FileUtility {

    private FileUtility() {}

    /**
     * Compress a byte array using gzip.
     *
     * @param input the byte array to compress
     * @return the compressed data
     */
    public static byte[] gZip(final byte[] input) throws IOException {
        final var compressedOut = new ByteArrayOutputStream();
        final var gZipOut = new GZIPOutputStream(compressedOut);
        gZipOut.write(input, 0, input.length);
        gZipOut.finish();
        gZipOut.close();
        return compressedOut.toByteArray();
    }
}
