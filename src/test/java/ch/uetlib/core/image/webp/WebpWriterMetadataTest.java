package ch.uetlib.core.image.webp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

class WebpWriterMetadataTest {

    @Test
    void createMetadataFileWritesTrimmedSingleLineUtf8() throws IOException {
        try (final var writer = new WebpWriter()) {
            final var file =
                    writer.createMetadataFile("\n  <x:xmpmeta>Zürich</x:xmpmeta>\r\n<a/>\n");

            assertEquals("<x:xmpmeta>Zürich</x:xmpmeta><a/>", Files.readString(file, UTF_8));
        }
    }
}
