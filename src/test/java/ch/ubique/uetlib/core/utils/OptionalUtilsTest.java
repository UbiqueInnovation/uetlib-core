package ch.ubique.uetlib.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class OptionalUtilsTest {

    @Test
    void testOfNullable() {
        final var value = "test";
        final var optional = OptionalUtils.ofNullable(value);
        assertTrue(optional.isPresent());
        assertEquals(value, optional.get());

        final var nullOptional = OptionalUtils.ofNullable(null);
        assertTrue(nullOptional.isEmpty());
    }

    @Test
    void testTryGet() {
        assertEquals(4.0, OptionalUtils.tryGet(() -> Double.parseDouble("4.0")).orElseThrow());
        assertFalse(OptionalUtils.tryGet(() -> Double.parseDouble("abc")).isPresent());
        assertFalse(
                OptionalUtils.tryGet(() -> Double.parseDouble("abc"), NumberFormatException.class)
                        .isPresent());
        assertFalse(
                OptionalUtils.tryGet(
                                () -> Double.parseDouble("abc"),
                                NumberFormatException.class,
                                NoSuchFieldException.class)
                        .isPresent());
        assertThrows(
                NumberFormatException.class,
                () ->
                        OptionalUtils.tryGet(
                                () -> Double.parseDouble("abc"), NullPointerException.class));

        assertThrows(
                RuntimeException.class,
                () ->
                        OptionalUtils.tryGet(
                                () -> {
                                    throw new RuntimeException();
                                },
                                NullPointerException.class));

        assertDoesNotThrow(
                () ->
                        OptionalUtils.tryGet(
                                () -> {
                                    throw new NullPointerException();
                                },
                                RuntimeException.class));

        assertEquals(
                Optional.empty(),
                OptionalUtils.tryGet(
                        () -> {
                            throw new Exception();
                        }));

        assertThrows(
                Exception.class,
                () ->
                        OptionalUtils.tryGet(
                                () -> {
                                    throw new Exception();
                                },
                                RuntimeException.class),
                "Only RuntimeException is allowed, but Exception is thrown. Expected to rethrow"
                        + " exception");
    }

    @Test
    void coalesce() {
        assertEquals(5, OptionalUtils.coalesce(null, 5, null).orElseThrow());
        assertEquals(
                5,
                OptionalUtils.coalesce(Optional.empty(), Optional.of(5), Optional.empty())
                        .orElseThrow());
        assertEquals(5, OptionalUtils.coalesce(() -> null, () -> 5, () -> null).orElseThrow());
        final var count = new AtomicInteger();
        assertEquals(
                1,
                OptionalUtils.coalesce(() -> null, count::incrementAndGet, count::incrementAndGet)
                        .orElseThrow());
        assertEquals(1, count.get());
    }
}
