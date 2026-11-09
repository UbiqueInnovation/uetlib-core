package ch.uetlib.core.file.watch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Disabled("does not work on mac")
class WatchManagerTest {

    @Test
    @SuppressWarnings("java:S2925")
    void testWatchManager() throws IOException, InterruptedException {
        final var testDir = "/tmp/watchtest";
        final var testDirPath = Paths.get(testDir);
        Files.createDirectories(testDirPath);

        final var testFile = testDir + "/test.txt";
        final var testFilePath = Paths.get(testFile);
        Files.deleteIfExists(testFilePath);

        final var watchService = FileSystems.getDefault().newWatchService();
        testDirPath.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        final var modified = new AtomicBoolean(false);
        final var deleted = new AtomicBoolean(false);
        final var created = new AtomicBoolean(false);

        final var wm =
                new WatchManager(
                        watchService,
                        new FileEventListener() {

                            @Override
                            public void onModified(final Path file) {
                                modified.set(true);
                            }

                            @Override
                            public void onDeleted(final Path file) {
                                deleted.set(true);
                            }

                            @Override
                            public void onCreated(final Path file) {
                                created.set(true);
                            }
                        });
        wm.watch();

        assertFalse(created.get());
        Files.createFile(testFilePath);
        Thread.sleep(Duration.ofMillis(100));
        assertTrue(created.get());

        assertFalse(modified.get());
        Files.writeString(testFilePath, "This is a test string for a test file");
        Thread.sleep(Duration.ofMillis(100));
        assertTrue(modified.get());

        assertFalse(deleted.get());
        Files.delete(testFilePath);
        Thread.sleep(Duration.ofMillis(100));
        assertTrue(deleted.get());

        wm.destroy();
    }
}
