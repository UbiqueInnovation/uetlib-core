package ch.ubique.backend.core.process;

import java.util.List;

public record FinishedProcessResult(int exitCode, List<String> stdout, List<String> stderr)
        implements ProcessResult {
    @Override
    public boolean didFinish() {
        return true;
    }
}
