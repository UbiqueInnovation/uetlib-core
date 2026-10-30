package ch.ubique.uetlib.core.utils;

import static ch.ubique.uetlib.core.io.CompressionAlgorithm.*;
import static ch.ubique.uetlib.core.utils.InputStreamUtils.findCompressionByContentEncoding;
import static ch.ubique.uetlib.core.utils.InputStreamUtils.findCompressionByFilePath;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

@SuppressWarnings("resource")
class InputStreamUtilsTest {

    @Test
    void pipeOutputToInputStreamWithContent() throws IOException {
        final var content = "I'm going through the pipe.";

        final var originOut = new ByteArrayOutputStream();
        originOut.write(content.getBytes());

        final var in = InputStreamUtils.pipeOutputToInputStream(originOut);
        final var inContent = new String(in.readAllBytes());
        assertEquals(content, inContent);
    }

    @Test
    void pipeOutputToInputStreamWithEmptyContent() throws IOException {
        final var originOut = new ByteArrayOutputStream();

        final var in = InputStreamUtils.pipeOutputToInputStream(originOut);
        assertArrayEquals(new byte[0], in.readAllBytes());
    }

    @Test
    void pipeOutputToInputStreamRejectsNull() {
        assertThrows(
                NullPointerException.class, () -> InputStreamUtils.pipeOutputToInputStream(null));
    }

    @Test
    void contentInfoPreservesStreamWhenMarkSupported() throws IOException {
        final var contentBytes = "content-info-test".getBytes(StandardCharsets.UTF_8);
        final var inputStream = new ByteArrayInputStream(contentBytes);

        InputStreamUtils.contentInfo(inputStream);

        final var remaining = inputStream.readAllBytes();
        assertArrayEquals(contentBytes, remaining);
    }

    @Test
    void contentInfoWorksWhenMarkNotSupported() {
        final var contentBytes = "content-info-test".getBytes(StandardCharsets.UTF_8);
        final var inputStream = new NoMarkInputStream(new ByteArrayInputStream(contentBytes));

        assertDoesNotThrow(() -> InputStreamUtils.contentInfo(inputStream));
    }

    @Test
    void findCompressionByContentEncodingHandlesKnownValues() {
        assertEquals(GZIP, findCompressionByContentEncoding("gzip"));
        assertEquals(BZIP2, findCompressionByContentEncoding("bzip2"));
        assertEquals(ZIP, findCompressionByContentEncoding("zip"));
        assertEquals(DEFLATE, findCompressionByContentEncoding("deflate"));
        assertEquals(COMPRESS_LZW, findCompressionByContentEncoding("compress"));
        assertEquals(BROTLI, findCompressionByContentEncoding("br"));
        assertNull(findCompressionByContentEncoding("unknown"));
    }

    @Test
    void findCompressionByFilePathHandlesKnownExtensions() {
        assertEquals(GZIP, findCompressionByFilePath("archive.gz"));
        assertEquals(BZIP2, findCompressionByFilePath("archive.bz2"));
        assertEquals(ZIP, findCompressionByFilePath("archive.zip"));
        assertNull(findCompressionByFilePath("archive"));
    }

    private static final class NoMarkInputStream extends FilterInputStream {
        private NoMarkInputStream(final InputStream in) {
            super(in);
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        @Override
        public synchronized void mark(final int readlimit) {
            throw new UnsupportedOperationException("mark not supported");
        }

        @Override
        public synchronized void reset() {
            throw new UnsupportedOperationException("reset not supported");
        }
    }
}
