package ch.uetlib.core.executor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

class VirtualThreadUtilsTest {

    private static Integer throwForThree(final int i) throws IOException {
        if (i == 3) {
            throw new IOException();
        }
        return i;
    }

    @Test
    void throwingForEach() {
        assertThrows(
                IOException.class,
                () ->
                        VirtualThreadUtils.throwingForEach(
                                i -> {
                                    if (i == 3) {
                                        throw new IOException();
                                    }
                                },
                                Stream.of(1, 2, 3, 4, 5)));
    }

    @Test
    void throwingMapAll() throws IOException {
        final var result =
                VirtualThreadUtils.throwingMapAll(
                        VirtualThreadUtilsTest::throwForThree, List.of(1, 2, 4, 5));
        assertEquals(List.of(1, 2, 4, 5), result);
        assertThrows(
                IOException.class,
                () ->
                        VirtualThreadUtils.throwingMapAll(
                                VirtualThreadUtilsTest::throwForThree, List.of(0, 1, 2, 3, 4)));
    }
}
