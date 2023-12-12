package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.process.FinishedProcessResult;
import ch.ubique.backend.core.process.ProcessConfig;
import ch.ubique.backend.core.process.ProcessResult;
import ch.ubique.backend.core.process.UnfinishedProcessResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProcessUtils {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    private ProcessUtils() {}

    /**
     * Executes a process and returns results from the process.
     *
     * @throws IOException If the process could not be started
     */
    public static ProcessResult executeProcess(final ProcessConfig config) throws IOException {
        try {
            logger.debug("Running command: {}", config.command());
            final var builder = new ProcessBuilder(config.command());
            if (config.directory() != null) {
                builder.directory(config.directory());
            }
            final var process = builder.start();
            final var stdout = new ConcurrentLinkedDeque<String>();
            final var stderr = new ConcurrentLinkedDeque<String>();
            Executors.newSingleThreadExecutor()
                    .submit(
                            () -> {
                                try (final var inputReader = process.inputReader()) {
                                    inputReader
                                            .lines()
                                            .forEach(
                                                    line -> {
                                                        logger.atLevel(config.logLevelStdout())
                                                                .log("stdout: {}", line);
                                                        stdout.add(line);
                                                    });
                                } catch (final IOException e) {
                                    logger.warn("Error reading stdout", e);
                                }
                            });
            Executors.newSingleThreadExecutor()
                    .submit(
                            () -> {
                                try (final var errorReader = process.errorReader()) {
                                    errorReader
                                            .lines()
                                            .forEach(
                                                    line -> {
                                                        logger.atLevel(config.logLevelStderr())
                                                                .log("stderr: {}", line);
                                                        stderr.add(line);
                                                    });
                                } catch (final IOException e) {
                                    logger.warn("Error reading stderr", e);
                                }
                            });
            final var didProcessFinish =
                    process.waitFor(config.timeout().toSeconds(), TimeUnit.SECONDS);
            if (!didProcessFinish) {
                return new UnfinishedProcessResult(
                        stdout.stream().toList(), stderr.stream().toList());
            }
            final var exitValue = process.exitValue();
            logger.debug("Process finished with exit value {}", exitValue);
            return new FinishedProcessResult(
                    exitValue, stdout.stream().toList(), stderr.stream().toList());
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
