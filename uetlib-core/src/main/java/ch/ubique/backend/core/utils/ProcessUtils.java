package ch.ubique.backend.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProcessUtils {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    private ProcessUtils() {}

    /**
     * Executes a process and returns the exit value. The process output is logged to stdout and
     * stderr at the default levels of {@link Level#DEBUG} and {@link Level#WARN}.
     *
     * @see #executeProcessAndLog(List, Duration, Level, Level)
     */
    public static int executeProcessAndLog(final List<String> command, final Duration timeout)
            throws IOException, IllegalStateException {
        return executeProcessAndLog(command, timeout, Level.DEBUG, Level.WARN);
    }

    /**
     * Executes a process and returns the exit value. The process output is logged to stdout and
     * stderr at the given levels.
     *
     * @param command The command to execute
     * @param timeout The timeout after which the process is killed
     * @param logLevelStdout The log level for stdout output from the process
     * @param logLevelStderr The log level for stderr output from the process
     * @return The exit value of the process
     * @throws IOException If the process could not be started
     * @throws IllegalStateException If the process exceeds the timeout
     */
    public static int executeProcessAndLog(
            final List<String> command,
            final Duration timeout,
            final Level logLevelStdout,
            final Level logLevelStderr)
            throws IOException, IllegalStateException {
        try {
            logger.debug("Running command: {}", command);
            final var builder = new ProcessBuilder(command);
            final var process = builder.start();
            Executors.newSingleThreadExecutor()
                    .submit(
                            () ->
                                    logProcessOutputsAndErrors(
                                            process, logLevelStdout, logLevelStderr));
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

    private static void logProcessOutputsAndErrors(
            final Process process, final Level logLevelStdout, final Level logLevelStderr) {
        try (final var inputReader = process.inputReader()) {
            inputReader
                    .lines()
                    .forEach(line -> logger.atLevel(logLevelStdout).log("stdout: {}", line));
        } catch (final IOException e) {
            logger.warn("Error reading stdout", e);
        }
        try (final var errorReader = process.errorReader()) {
            errorReader
                    .lines()
                    .forEach(line -> logger.atLevel(logLevelStderr).log("stderr: {}", line));
        } catch (final IOException e) {
            logger.warn("Error reading stderr", e);
        }
    }
}
