package ch.ubique.backend.core.file.watch;

import java.nio.file.Path;

public interface FileEventListener {

    void onCreated(Path file);

    void onDeleted(Path file);

    void onModified(Path file);
}
