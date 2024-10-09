package ch.ubique.backend.core.http.file;

import ch.ubique.backend.core.file.ByteFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unused")
public final class FileUtil {

    private FileUtil() {}

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

    public static ByteFile objectToByteFile(
            final ObjectMapper objectMapper, final Object object, final String fileName)
            throws IOException {
        final var bytes = objectMapper.writeValueAsBytes(object);
        return new ByteFile()
                .setBytes(bytes)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .setFileName(fileName);
    }
}
