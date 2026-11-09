package ch.uetlib.core.process;

import java.io.IOException;
import java.util.List;

public record FinishedProcessResult(
        int exitCode, List<String> command, List<String> stdout, List<String> stderr)
        implements ProcessResult {
    @Override
    public boolean didFinish() {
        return true;
    }

    @Override
    public boolean success() {
        return exitCode == 0;
    }

    @Override
    public void throwIfNotSuccessful() throws IOException {
        if (!success()) {
            throw new IOException(
                    String.format(
                            "Process finished with exit code %d."
                                    + "%nCommand:%n%s"
                                    + "%nStdout:%n%s"
                                    + "%nStderr:%n%s",
                            exitCode,
                            String.join(" ", command),
                            String.join("\n", stdout),
                            String.join("\n", stderr)));
        }
    }
}
