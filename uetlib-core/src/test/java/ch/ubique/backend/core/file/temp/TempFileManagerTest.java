package ch.ubique.backend.core.file.temp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

class TempFileManagerTest {

    @Test
    void testCreateTempFiles() {
        final var testString = "teststringfortenpfilecreations";

        assertDoesNotThrow(
                () -> {
                    final var tempFileBytes =
                            TempFileManager.createTempFile(
                                    testString.getBytes(StandardCharsets.UTF_8), ".tmp");
                    final var tempFileStream =
                            TempFileManager.createTempFile(
                                    new ByteArrayInputStream(
                                            testString.getBytes(StandardCharsets.UTF_8)),
                                    ".tmp");

                    assertNotNull(tempFileBytes);
                    assertNotNull(tempFileStream);
                    assertTrue(TempFileManager.getTempFiles().contains(tempFileBytes));
                    assertTrue(TempFileManager.getTempFiles().contains(tempFileStream));
                });

        TempFileManager.deleteTempFiles();
    }
}
