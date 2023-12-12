package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.process.FinishedProcessResult;
import ch.ubique.backend.core.process.ProcessConfig;
import ch.ubique.backend.core.process.ProcessResult;
import ch.ubique.backend.core.process.UnfinishedProcessResult;
import com.pivovarit.function.exception.WrappedException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ProcessUtils {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    private ProcessUtils() {
    }

    public static ProcessResult executeProcess(final List<String> command) throws IOException {
        return executeProcess(ProcessConfig.builder().command(command).build());
    }

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
                builder.directory(config.directory().toFile());
            }
            final var process = builder.start();
            final var stdoutFuture =
                    Executors.newSingleThreadExecutor()
                            .submit(
                                    () ->
                                            processOutputLines(
                                                    process::inputReader,
                                                    config.logLevelStdout(),
                                                    "stdout"));
            final var stderrFuture =
                    Executors.newSingleThreadExecutor()
                            .submit(
                                    () ->
                                            processOutputLines(
                                                    process::errorReader,
                                                    config.logLevelStderr(),
                                                    "stderr"));
            final var didProcessFinish =
                    process.waitFor(config.timeout().toSeconds(), TimeUnit.SECONDS);

            final var stdout = stdoutFuture.get().map(String.class::cast).toList();
            final var stderr = stderrFuture.get().map(String.class::cast).toList();

            if (!didProcessFinish) {
                return new UnfinishedProcessResult(stdout, stderr);
            }
            final var exitValue = process.exitValue();
            logger.debug("Process finished with exit value {}", exitValue);
            return new FinishedProcessResult(
                    exitValue, stdout.stream().toList(), stderr.stream().toList());
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WrappedException(e);
        } catch (final ExecutionException e) {
            throw new WrappedException(e);
        }
    }

    @NotNull
    private static Stream<?> processOutputLines(
            final Supplier<BufferedReader> readerSupplier,
            final Level config,
            final String outputStreamName) {
        try (final var reader = readerSupplier.get()) {
            //noinspection SimplifyStreamApiCallChains
            return reader.lines()
                    .map(
                            line -> {
                                logger.atLevel(config).log("{}: {}", outputStreamName, line);
                                return line;
                            });
        } catch (final IOException e) {
            logger.warn("Error reading {}", outputStreamName, e);
        }
        return Stream.of();
    }
}
