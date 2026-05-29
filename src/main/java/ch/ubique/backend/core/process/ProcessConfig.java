package ch.ubique.backend.core.process;

import org.apache.commons.collections4.CollectionUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.event.Level;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class ProcessConfig {
    private final List<String> command;
    private final Duration timeout;
    private final @Nullable Path directory;
    private final Level logLevelStdout;
    private final Level logLevelStderr;

    private ProcessConfig(
            final List<String> command,
            final Duration timeout,
            final @Nullable Path directory,
            final Level logLevelStdout,
            final Level logLevelStderr) {
        this.command = command;
        this.timeout = timeout;
        this.directory = directory;
        this.logLevelStdout = logLevelStdout;
        this.logLevelStderr = logLevelStderr;
    }

    public static ProcessConfigBuilder builder() {
        return new ProcessConfigBuilder();
    }

    public List<String> command() {
        return command;
    }

    public Duration timeout() {
        return timeout;
    }

    public @Nullable Path directory() {
        return directory;
    }

    public Level logLevelStdout() {
        return logLevelStdout;
    }

    public Level logLevelStderr() {
        return logLevelStderr;
    }

    public static class ProcessConfigBuilder {
        private @Nullable List<String> command;
        private Duration timeout;
        private @Nullable Path directory;
        private Level logLevelStdout;
        private Level logLevelStderr;

        private ProcessConfigBuilder() {
            this.timeout = Duration.ofSeconds(10);
            this.logLevelStdout = Level.DEBUG;
            this.logLevelStderr = Level.WARN;
        }

        public ProcessConfigBuilder command(final List<String> command) {
            this.command = command;
            return this;
        }

        public ProcessConfigBuilder timeout(final Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public ProcessConfigBuilder directory(final Path directory) {
            this.directory = directory;
            return this;
        }

        public ProcessConfigBuilder logLevelStdout(final Level logLevelStdout) {
            this.logLevelStdout = logLevelStdout;
            return this;
        }

        public ProcessConfigBuilder logLevelStderr(final Level logLevelStderr) {
            this.logLevelStderr = logLevelStderr;
            return this;
        }

        /**
         * Builds the {@link ProcessConfig} instance.
         *
         * @throws IllegalStateException if the command has not been set
         */
        public ProcessConfig build() throws IllegalStateException {
            if (CollectionUtils.isEmpty(command)) {
                throw new IllegalStateException("command must not be null or empty");
            }
            return new ProcessConfig(command, timeout, directory, logLevelStdout, logLevelStderr);
        }
    }
}
