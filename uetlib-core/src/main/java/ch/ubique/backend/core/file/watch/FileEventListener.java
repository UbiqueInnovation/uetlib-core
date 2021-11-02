package ch.ubique.backend.core.file.watch;

import java.nio.file.Path;

public interface FileEventListener {

    public void onCreated(Path file);

    public void onDeleted(Path file);

    public void onModified(Path file);
}
