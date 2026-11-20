package ch.uetlib.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.uetlib.core.process.ProcessConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@DisabledOnOs(OS.WINDOWS)
class ProcessUtilsTest {

    @Test
    void finishedProcessReturnsItsOutput() throws IOException {
        final var result = ProcessUtils.executeProcess(List.of("echo", "hello"));

        assertTrue(result.didFinish());
        assertTrue(result.success());
        assertEquals(List.of("hello"), result.stdout());
    }

    @Test
    void processExceedingTheSubSecondTimeoutIsUnfinished() throws IOException {
        final var config =
                ProcessConfig.builder()
                        .command(List.of("sleep", "1"))
                        .timeout(Duration.ofMillis(200))
                        .build();

        final var result = ProcessUtils.executeProcess(config);

        assertFalse(result.didFinish());
    }
}
