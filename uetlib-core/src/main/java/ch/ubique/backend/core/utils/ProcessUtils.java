package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.process.FinishedProcessResult;
import ch.ubique.backend.core.process.ProcessResult;
import ch.ubique.backend.core.process.UnfinishedProcessResult;

import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ProcessUtils {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    private ProcessUtils() {}

    public static ProcessResult executeProcess(final List<String> command, final Duration timeout)
            throws IOException {
        final var stdout = new ArrayList<String>();
        final var stderr = new ArrayList<String>();
        try {
            final var exitCode =
                    executeProcessAndHandleLogs(
                            command,
                            timeout,
                            process -> collectProcessOutputsAndErrors(process, stdout, stderr));
            return new FinishedProcessResult(exitCode, stdout, stderr);
        } catch (final IllegalStateException e) {
            return new UnfinishedProcessResult(stdout, stderr, e);
        }
    }

    /**
     * Executes a process and returns the exit value. The process output is logged to stdout and
     * stderr at the default levels of {@link Level#DEBUG} and {@link Level#WARN}.
     *
     * @see #executeProcessAndHandleLogs(List, Duration, Consumer)
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
     * @param logLevelStdout The log level for stdout
     * @param logLevelStderr The log level for stderr
     * @return The exit value of the process
     * @throws IOException If the process could not be started
     * @throws IllegalStateException If the process exceeds the timeout
     * @see #executeProcessAndHandleLogs(List, Duration, Consumer)
     */
    public static int executeProcessAndLog(
            final List<String> command,
            final Duration timeout,
            final Level logLevelStdout,
            final Level logLevelStderr)
            throws IOException, IllegalStateException {
        return executeProcessAndHandleLogs(
                command,
                timeout,
                process -> logProcessOutputsAndErrors(process, logLevelStdout, logLevelStderr));
    }

    /**
     * Executes a process and returns the exit value. The process output is logged to stdout and
     * stderr at the given levels.
     *
     * @param command The command to execute
     * @param timeout The timeout after which the process is killed
     * @param processLogHandler A handler for the process output is passed the process as argument
     * @return The exit value of the process
     * @throws IOException If the process could not be started
     * @throws IllegalStateException If the process exceeds the timeout
     */
    private static int executeProcessAndHandleLogs(
            final List<String> command,
            final Duration timeout,
            final Consumer<Process> processLogHandler)
            throws IOException, IllegalStateException {
        try {
            logger.debug("Running command: {}", command);
            final var builder = new ProcessBuilder(command);
            final var process = builder.start();
            Executors.newSingleThreadExecutor().submit(() -> processLogHandler.accept(process));
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

    private static Pair<List<String>, List<String>> collectProcessOutputsAndErrors(
            final Process process,
            @NotNull final List<String> stdout,
            @NotNull final List<String> stderr) {
        try (final var inputReader = process.inputReader()) {
            stdout.addAll(inputReader.lines().toList());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        try (final var errorReader = process.errorReader()) {
            stderr.addAll(errorReader.lines().toList());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return Pair.of(stdout, stderr);
    }
}
