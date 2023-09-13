package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class InputStreamUtilsTest {

    @Test
    void pipeOutputToInputStream() throws IOException {
        final var content = "I'm going through the pipe.";

        final var originOut = new ByteArrayOutputStream();
        originOut.write(content.getBytes());

        final var in = InputStreamUtils.pipeOutputToInputStream(originOut);
        final var inContent = new String(in.readAllBytes());
        assertEquals(content, inContent);
    }
}
