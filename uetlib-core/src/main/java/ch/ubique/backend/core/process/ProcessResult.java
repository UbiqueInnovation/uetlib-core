package ch.ubique.backend.core.process;

import java.util.List;

public sealed interface ProcessResult permits FinishedProcessResult, UnfinishedProcessResult {
    boolean didFinish();

    List<String> stdout();

    List<String> stderr();
}
