package ch.ubique.uetlib.core.file.watch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchManager implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WatchManager.class);

    private final WatchService watchService;
    private final FileEventListener listener;

    private final Thread thread;

    public WatchManager(final WatchService watchService, final FileEventListener listener) {
        this.watchService = watchService;
        this.listener = listener;
        thread = new Thread(this);
        thread.setDaemon(true);
    }

    public void destroy() {
        try {
            thread.interrupt();
            watchService.close();
        } catch (final IOException ignored) {
            // ignore exception
        }
    }

    public void watch() {
        logger.info("Start watch manager: {}", watchService);
        thread.start();
    }

    @Override
    public void run() {
        var poll = true;
        while (poll) {
            try {
                poll = pollEvents(watchService);
            } catch (final InterruptedException | ClosedWatchServiceException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean pollEvents(final WatchService watchService) throws InterruptedException {
        final var key = watchService.take();
        final var path = (Path) key.watchable();
        for (final var event : key.pollEvents()) {
            notifyListeners(event.kind(), path.resolve((Path) event.context()));
        }
        return key.reset();
    }

    private void notifyListeners(final Kind<?> kind, final Path file) {
        logger.info("New file event: {} Event: {}", file, kind);
        if (kind == ENTRY_CREATE) {
            listener.onCreated(file);
        } else if (kind == ENTRY_MODIFY) {
            listener.onModified(file);
        } else if (kind == ENTRY_DELETE) {
            listener.onDeleted(file);
        }
    }
}
