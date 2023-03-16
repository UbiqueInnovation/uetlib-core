package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.utils.MinMaxCollector.MinMax;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

class MinMaxCollectorTest {

    @Test
    @SuppressWarnings("java:S5778")
    void testMinMax() {
        //noinspection ResultOfMethodCallIgnored
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> Stream.<Integer>of().collect(MinMaxCollector.collect()));
        Assertions.assertEquals(
                new MinMax<>(56, 56), Stream.of(56).collect(MinMaxCollector.collect()));
        Assertions.assertEquals(
                new MinMax<>(56, 56), Stream.of(56, 56).collect(MinMaxCollector.collect()));
        Assertions.assertEquals(
                new MinMax<>(56, 57), Stream.of(56, 57).collect(MinMaxCollector.collect()));
        Assertions.assertEquals(
                new MinMax<>(-2, 869),
                Stream.of(56, 57, -2, 869, 0).collect(MinMaxCollector.collect()));
        Assertions.assertEquals(
                new MinMax<>(Integer.MIN_VALUE, Integer.MAX_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(MinMaxCollector.collect()));

        //noinspection ResultOfMethodCallIgnored
        Assertions.assertThrows(
                NoSuchElementException.class,
                () ->
                        Stream.<Integer>of()
                                .collect(
                                        MinMaxCollector.collect(
                                                Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56).collect(MinMaxCollector.collect(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56)
                        .collect(MinMaxCollector.collect(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(57, 56),
                Stream.of(56, 57)
                        .collect(MinMaxCollector.collect(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(869, -2),
                Stream.of(56, 57, -2, 869, 0)
                        .collect(MinMaxCollector.collect(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(Integer.MAX_VALUE, Integer.MIN_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(MinMaxCollector.collect(Comparator.<Integer>reverseOrder())));
    }

    @Test
    void testOptional() {
        Assertions.assertEquals(
                Optional.empty(), Stream.<Integer>of().collect(MinMaxCollector.collectOptional()));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56).collect(MinMaxCollector.collectOptional()).orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56).collect(MinMaxCollector.collectOptional()).orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(56, 57),
                Stream.of(56, 57).collect(MinMaxCollector.collectOptional()).orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(-2, 869),
                Stream.of(56, 57, -2, 869, 0)
                        .collect(MinMaxCollector.collectOptional())
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(Integer.MIN_VALUE, Integer.MAX_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(MinMaxCollector.collectOptional())
                        .orElseThrow());

        Assertions.assertEquals(
                Optional.empty(),
                Stream.<Integer>of()
                        .collect(
                                MinMaxCollector.collectOptional(
                                        Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56)
                        .collect(
                                MinMaxCollector.collectOptional(Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56)
                        .collect(
                                MinMaxCollector.collectOptional(Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(57, 56),
                Stream.of(56, 57)
                        .collect(
                                MinMaxCollector.collectOptional(Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(869, -2),
                Stream.of(56, 57, -2, 869, 0)
                        .collect(
                                MinMaxCollector.collectOptional(Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(Integer.MAX_VALUE, Integer.MIN_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(
                                MinMaxCollector.collectOptional(Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
    }

    @Test
    void testMinNullable() {
        Assertions.assertNull(MinMaxCollector.minNullable(null, null));
        Assertions.assertEquals(2, MinMaxCollector.minNullable(2, null));
        Assertions.assertEquals(5, MinMaxCollector.minNullable(null, 5));
        Assertions.assertEquals(4, MinMaxCollector.minNullable(4, 5));
        Assertions.assertEquals(5, MinMaxCollector.minNullable(5, 5));
        Assertions.assertEquals(-10, MinMaxCollector.minNullable(-10, 5));
        Assertions.assertEquals(-10, MinMaxCollector.minNullable(10, -10));
        Assertions.assertEquals(
                Integer.MIN_VALUE,
                MinMaxCollector.minNullable(Integer.MIN_VALUE, Integer.MAX_VALUE));

        Assertions.assertNull(
                MinMaxCollector.minNullable(null, null, Comparator.<Integer>reverseOrder()));
        Assertions.assertEquals(2, MinMaxCollector.minNullable(2, null, Comparator.reverseOrder()));
        Assertions.assertEquals(5, MinMaxCollector.minNullable(null, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, MinMaxCollector.minNullable(4, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, MinMaxCollector.minNullable(5, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, MinMaxCollector.minNullable(-10, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(
                10, MinMaxCollector.minNullable(10, -10, Comparator.reverseOrder()));
        Assertions.assertEquals(
                Integer.MAX_VALUE,
                MinMaxCollector.minNullable(
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Comparator.reverseOrder()));
    }

    @Test
    void testMaxNullable() {
        Assertions.assertNull(MinMaxCollector.maxNullable(null, null));
        Assertions.assertEquals(2, MinMaxCollector.maxNullable(2, null));
        Assertions.assertEquals(5, MinMaxCollector.maxNullable(null, 5));
        Assertions.assertEquals(5, MinMaxCollector.maxNullable(4, 5));
        Assertions.assertEquals(5, MinMaxCollector.maxNullable(5, 5));
        Assertions.assertEquals(5, MinMaxCollector.maxNullable(-10, 5));
        Assertions.assertEquals(10, MinMaxCollector.maxNullable(10, -10));
        Assertions.assertEquals(
                Integer.MAX_VALUE,
                MinMaxCollector.maxNullable(Integer.MIN_VALUE, Integer.MAX_VALUE));

        Assertions.assertNull(
                MinMaxCollector.maxNullable(null, null, Comparator.<Integer>reverseOrder()));
        Assertions.assertEquals(2, MinMaxCollector.maxNullable(2, null, Comparator.reverseOrder()));
        Assertions.assertEquals(5, MinMaxCollector.maxNullable(null, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(4, MinMaxCollector.maxNullable(4, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, MinMaxCollector.maxNullable(5, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(
                -10, MinMaxCollector.maxNullable(-10, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(
                -10, MinMaxCollector.maxNullable(10, -10, Comparator.reverseOrder()));
        Assertions.assertEquals(
                Integer.MIN_VALUE,
                MinMaxCollector.maxNullable(
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Comparator.reverseOrder()));
    }
}
