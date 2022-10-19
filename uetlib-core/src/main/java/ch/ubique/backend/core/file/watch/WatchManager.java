package ch.ubique.backend.core.file.watch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

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

    private Thread thread;

    public WatchManager(WatchService watchService, FileEventListener listener) {
        this.watchService = watchService;
        this.listener = listener;
    }

    public void destroy() {
        try {
            thread.interrupt();
            watchService.close();
        } catch (IOException ignored) {
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
            } catch (InterruptedException | ClosedWatchServiceException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean pollEvents(WatchService watchService) throws InterruptedException {
        WatchKey key = watchService.take();
        Path path = (Path) key.watchable();
        for (WatchEvent<?> event : key.pollEvents()) {
            notifyListeners(event.kind(), path.resolve((Path) event.context()));
        }
        return key.reset();
    }

    private void notifyListeners(Kind<?> kind, Path file) {
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
