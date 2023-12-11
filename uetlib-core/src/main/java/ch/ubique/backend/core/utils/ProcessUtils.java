package ch.ubique.backend.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProcessUtils {

    private ProcessUtils() {}

    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    public static int executeProcess(final List<String> command, final Duration timeout)
            throws IOException {

        try {
            logger.debug("Running command: {}", command);
            final var builder = new ProcessBuilder(command);
            final var process = builder.start();
            Executors.newSingleThreadExecutor().submit(() -> logProcessOutputsAndErrors(process));
            final var didProcessFinish = process.waitFor(timeout.toSeconds(), TimeUnit.SECONDS);
            if (!didProcessFinish) {
                throw new IllegalStateException(process + " exceeded timeout.");
            }
            final var exitValue = process.exitValue();
            logger.debug("Process finished with exit value {}", exitValue);
            return exitValue;
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private static void logProcessOutputsAndErrors(final Process process) {
        try (final var inputReader = process.inputReader()) {
            inputReader.lines().forEach(line -> logger.debug("stdout: {}", line));
        } catch (final IOException e) {
            logger.warn("Error reading stdout", e);
        }
        try (final var errorReader = process.errorReader()) {
            errorReader.lines().forEach(line -> logger.warn("stderr: {}", line));
        } catch (final IOException e) {
            logger.warn("Error reading stderr", e);
        }
    }
}
