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
        final var tempFileManager = new TempFileManager();
        assertDoesNotThrow(
                () -> {
                    final var tempFileBytes =
                            tempFileManager.createTempFile(
                                    testString.getBytes(StandardCharsets.UTF_8), ".tmp");
                    final var tempFileStream =
                            tempFileManager.createTempFile(
                                    new ByteArrayInputStream(
                                            testString.getBytes(StandardCharsets.UTF_8)),
                                    ".tmp");

                    assertNotNull(tempFileBytes);
                    assertNotNull(tempFileStream);
                    assertTrue(tempFileManager.getTempFiles().contains(tempFileBytes));
                    assertTrue(tempFileManager.getTempFiles().contains(tempFileStream));
                });

        tempFileManager.deleteTempFiles();
    }
}
