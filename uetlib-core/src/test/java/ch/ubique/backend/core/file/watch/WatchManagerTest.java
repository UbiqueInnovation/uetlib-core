package ch.ubique.backend.core.file.watch;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

class WatchManagerTest {

	@Test
	void test() throws IOException, InterruptedException {
		Path testFile = Paths.get("/tmp/watchtest/test.txt");
		Files.deleteIfExists(testFile);
		
		WatchService watchService = FileSystems.getDefault().newWatchService();
		Path path = Paths.get("/tmp/watchtest");
		Files.createDirectories(path);

		path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

		final AtomicBoolean modified = new AtomicBoolean(false);
		final AtomicBoolean deleted = new AtomicBoolean(false);
		final AtomicBoolean created = new AtomicBoolean(false);

		WatchManager wm = new WatchManager(watchService, new FileEventListener() {

			@Override
			public void onModified(Path file) {
				modified.set(true);;
			}

			@Override
			public void onDeleted(Path file) {
				deleted.set(true);
			}

			@Override
			public void onCreated(Path file) {
				created.set(true);
			}
		});		
		wm.watch();
		
		Files.createFile(Paths.get("/tmp/watchtest/test.txt"));
		Thread.sleep(100l);
		assertTrue(created.get());
		
		Files.writeString(testFile, "This is a test string for a test file");
		Thread.sleep(100l);
		assertTrue(modified.get());
		
		Files.delete(testFile);
		Thread.sleep(100l);
		assertTrue(deleted.get());
		
		wm.destroy();
	}

}
