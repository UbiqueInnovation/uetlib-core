package ch.ubique.backend.core.utils;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StreamUtilsTest {

    @Test
    void chunked() {
        Assertions.assertEquals(List.of(), StreamUtils.chunked((List<Integer>) null, 3).toList());
        Assertions.assertEquals(
                List.of(List.of('T', 'e')), StreamUtils.chunked(List.of('T', 'e'), 3).toList());
        Assertions.assertEquals(
                List.of(List.of('T', 'e', 's')),
                StreamUtils.chunked(List.of('T', 'e', 's'), 3).toList());
        Assertions.assertEquals(
                List.of(), StreamUtils.chunked(List.of('T', 'e', 's'), -1).toList());
        Assertions.assertEquals(
                List.of(List.of('T', 'e', 's'), List.of('t', 'T', 'e'), List.of('s', 't')),
                StreamUtils.chunked(List.of('T', 'e', 's', 't', 'T', 'e', 's', 't'), 3).toList());

        Assertions.assertEquals(List.of(), StreamUtils.chunked((Stream<Integer>) null, 3).toList());
        Assertions.assertEquals(
                List.of(List.of('T', 'e')), StreamUtils.chunked(Stream.of('T', 'e'), 3).toList());
        Assertions.assertEquals(
                List.of(List.of('T', 'e', 's')),
                StreamUtils.chunked(Stream.of('T', 'e', 's'), 3).toList());
        Assertions.assertEquals(
                List.of(), StreamUtils.chunked(Stream.of('T', 'e', 's'), -1).toList());
        Assertions.assertEquals(
                List.of(List.of('T', 'e', 's'), List.of('t', 'T', 'e'), List.of('s', 't')),
                StreamUtils.chunked(Stream.of('T', 'e', 's', 't', 'T', 'e', 's', 't'), 3).toList());
    }

    @Test
    void windows() {
        Assertions.assertEquals(List.of(), StreamUtils.windows(null, 3).toList());
        Assertions.assertEquals(List.of(), StreamUtils.windows(List.of('T', 'e'), 3).toList());
        Assertions.assertEquals(
                List.of(List.of('T', 'e', 's')),
                StreamUtils.windows(List.of('T', 'e', 's'), 3).toList());
        Assertions.assertEquals(
                List.of(), StreamUtils.windows(List.of('T', 'e', 's'), -1).toList());
        Assertions.assertEquals(
                List.of(
                        List.of('T', 'e', 's'),
                        List.of('e', 's', 't'),
                        List.of('s', 't', 'T'),
                        List.of('t', 'T', 'e'),
                        List.of('T', 'e', 's'),
                        List.of('e', 's', 't')),
                StreamUtils.windows(List.of('T', 'e', 's', 't', 'T', 'e', 's', 't'), 3).toList());
    }
}
