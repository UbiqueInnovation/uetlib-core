package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

class StreamUtilsTest {

    @Test
    void chunked() {
        assertEquals(List.of(), StreamUtils.chunked((List<Integer>) null, 3).toList());
        assertEquals(
                List.of(List.of('T', 'e')), StreamUtils.chunked(List.of('T', 'e'), 3).toList());
        assertEquals(
                List.of(List.of('T', 'e', 's')),
                StreamUtils.chunked(List.of('T', 'e', 's'), 3).toList());
        assertEquals(List.of(), StreamUtils.chunked(List.of('T', 'e', 's'), -1).toList());
        assertEquals(
                List.of(List.of('T', 'e', 's'), List.of('t', 'T', 'e'), List.of('s', 't')),
                StreamUtils.chunked(List.of('T', 'e', 's', 't', 'T', 'e', 's', 't'), 3).toList());

        assertEquals(List.of(), StreamUtils.chunked((Stream<Integer>) null, 3).toList());
        assertEquals(
                List.of(List.of('T', 'e')), StreamUtils.chunked(Stream.of('T', 'e'), 3).toList());
        assertEquals(
                List.of(List.of('T', 'e', 's')),
                StreamUtils.chunked(Stream.of('T', 'e', 's'), 3).toList());
        assertEquals(List.of(), StreamUtils.chunked(Stream.of('T', 'e', 's'), -1).toList());
        assertEquals(
                List.of(List.of('T', 'e', 's'), List.of('t', 'T', 'e'), List.of('s', 't')),
                StreamUtils.chunked(Stream.of('T', 'e', 's', 't', 'T', 'e', 's', 't'), 3).toList());
    }

    @Test
    void windows() {
        assertEquals(List.of(), StreamUtils.windows(null, 3).toList());
        assertEquals(List.of(), StreamUtils.windows(List.of('T', 'e'), 3).toList());
        assertEquals(
                List.of(List.of('T', 'e', 's')),
                StreamUtils.windows(List.of('T', 'e', 's'), 3).toList());
        assertEquals(List.of(), StreamUtils.windows(List.of('T', 'e', 's'), -1).toList());
        assertEquals(
                List.of(
                        List.of('T', 'e', 's'),
                        List.of('e', 's', 't'),
                        List.of('s', 't', 'T'),
                        List.of('t', 'T', 'e'),
                        List.of('T', 'e', 's'),
                        List.of('e', 's', 't')),
                StreamUtils.windows(List.of('T', 'e', 's', 't', 'T', 'e', 's', 't'), 3).toList());
    }

    @Test
    void emptyIfNull() {
        assertEquals(List.of(), StreamUtils.emptyIfNull((Collection<Object>) null).toList());
        assertEquals(List.of(), StreamUtils.emptyIfNull(List.of()).toList());
        assertEquals(List.of(), StreamUtils.emptyIfNull(Set.of()).toList());
        assertEquals(List.of(1), StreamUtils.emptyIfNull(List.of(1)).toList());
        assertEquals(List.of(1), StreamUtils.emptyIfNull(Set.of(1)).toList());

        assertEquals(List.of(), StreamUtils.emptyIfNull((Object[]) null).toList());
        assertEquals(List.of(), StreamUtils.emptyIfNull(new Object[] {}).toList());
        assertEquals(List.of(1), StreamUtils.emptyIfNull(new Object[] {1}).toList());

        assertEquals(List.of(), StreamUtils.emptyIfNull((int[]) null).boxed().toList());
        assertEquals(List.of(), StreamUtils.emptyIfNull(new int[] {}).boxed().toList());
        assertEquals(List.of(1), StreamUtils.emptyIfNull(new int[] {1}).boxed().toList());

        assertEquals(List.of(), StreamUtils.emptyIfNull((long[]) null).boxed().toList());
        assertEquals(List.of(), StreamUtils.emptyIfNull(new long[] {}).boxed().toList());
        assertEquals(List.of(1L), StreamUtils.emptyIfNull(new long[] {1L}).boxed().toList());

        assertEquals(List.of(), StreamUtils.emptyIfNull((double[]) null).boxed().toList());
        assertEquals(List.of(), StreamUtils.emptyIfNull(new double[] {}).boxed().toList());
        assertEquals(List.of(1.0), StreamUtils.emptyIfNull(new double[] {1.0}).boxed().toList());
    }
}
