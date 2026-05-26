package ch.ubique.backend.core.file.watch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.util.Objects.requireNonNull;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class WatchManager implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WatchManager.class);

    private final WatchService watchService;
    private final FileEventListener listener;

    @Nullable private Thread thread;

    public WatchManager(final WatchService watchService, final FileEventListener listener) {
        this.watchService = watchService;
        this.listener = listener;
    }

    public void destroy() {
        try {
            requireNonNull(thread).interrupt();
            watchService.close();
        } catch (final IOException ignored) {
            // ignore exception
        }
    }

    public void watch() {
        logger.info("Start watch manager: {}", watchService);
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        boolean poll = true;
        while (poll) {
            try {
                poll = pollEvents(watchService);
            } catch (final InterruptedException | ClosedWatchServiceException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean pollEvents(final WatchService watchService) throws InterruptedException {
        final WatchKey key = watchService.take();
        final Path path = (Path) key.watchable();
        for (final WatchEvent<?> event : key.pollEvents()) {
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
