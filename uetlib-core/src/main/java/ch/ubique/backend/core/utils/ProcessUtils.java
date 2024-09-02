package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.process.FinishedProcessResult;
import ch.ubique.backend.core.process.ProcessConfig;
import ch.ubique.backend.core.process.ProcessResult;
import ch.ubique.backend.core.process.UnfinishedProcessResult;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pivovarit.function.exception.WrappedException;

import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ProcessUtils {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    private ProcessUtils() {}

    public static ProcessResult executeProcess(final List<String> command) throws IOException {
        return executeProcess(ProcessConfig.builder().command(command).build());
    }

    /**
     * Executes a process and returns results from the process.
     *
     * @throws IOException If the process could not be started
     */
    @SuppressWarnings("java:S2095")
    public static ProcessResult executeProcess(final ProcessConfig config) throws IOException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Running command '{}'", String.join(" ", config.command()));
            }
            final var builder = new ProcessBuilder(config.command());
            Optional.ofNullable(config.directory()).map(Path::toFile).ifPresent(builder::directory);
            final var process = builder.start();
            final var stdoutFuture =
                    Executors.newSingleThreadExecutor(threadFactory("process-stdout"))
                            .submit(
                                    () ->
                                            processOutputLines(
                                                    process::inputReader,
                                                    config.logLevelStdout(),
                                                    "stdout"));
            final var stderrFuture =
                    Executors.newSingleThreadExecutor(threadFactory("process-stderr"))
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
                return new UnfinishedProcessResult(config.command(), stdout, stderr);
            }
            final var exitValue = process.exitValue();
            logger.debug("Process finished with exit value {}", exitValue);
            return new FinishedProcessResult(exitValue, config.command(), stdout, stderr);
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
            final Level level,
            final String outputStreamName) {
        final var reader = readerSupplier.get();
        //noinspection SimplifyStreamApiCallChains
        return reader.lines()
                .map(
                        line -> {
                            logger.atLevel(level).log("{}: {}", outputStreamName, line);
                            return line;
                        });
    }

    private static @NotNull ThreadFactory threadFactory(final String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }
}
