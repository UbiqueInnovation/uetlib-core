package ch.ubique.uetlib.core.process;

import java.io.IOException;
import java.util.List;

public sealed interface ProcessResult permits FinishedProcessResult, UnfinishedProcessResult {
    boolean didFinish();

    List<String> stdout();

    List<String> stderr();

    boolean success();

    void throwIfNotSuccessful() throws IOException;
}
