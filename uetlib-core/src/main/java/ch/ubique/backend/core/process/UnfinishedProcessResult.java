package ch.ubique.backend.core.process;

import java.io.IOException;
import java.util.List;

public record UnfinishedProcessResult(
        List<String> command, List<String> stdout, List<String> stderr) implements ProcessResult {
    @Override
    public boolean didFinish() {
        return false;
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public void throwIfNotSuccessful() throws IOException {
        throw new IOException(
                String.format(
                        "Process did not finish."
                                + "%nCommand:%n%s"
                                + "%nStdout:%n%s"
                                + "%nStderr:%n%s",
                        String.join(" ", command),
                        String.join("\n", stdout),
                        String.join("\n", stderr)));
    }
}
