package ch.ubique.uetlib.core.utils;

import ch.ubique.uetlib.core.utils.MinMaxCollector.MinMax;
import ch.ubique.uetlib.core.utils.MinMaxCollector.MinMaxAccumulator;

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
    void testMerge() {
        final Comparator<Integer> comparator = Comparator.naturalOrder();
        Assertions.assertEquals(
                Optional.empty(),
                MinMaxAccumulator.<Integer>of(null)
                        .merge(MinMaxAccumulator.of(null), comparator)
                        .minMaxMaybe());
        Assertions.assertEquals(
                new MinMax<>(2, 2),
                MinMaxAccumulator.of(2)
                        .merge(MinMaxAccumulator.of(null), comparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(5, 5),
                MinMaxAccumulator.<Integer>of(null)
                        .merge(MinMaxAccumulator.of(5), comparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(4, 5),
                MinMaxAccumulator.of(4)
                        .merge(MinMaxAccumulator.of(5), comparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(5, 5),
                MinMaxAccumulator.of(5)
                        .merge(MinMaxAccumulator.of(5), comparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(-10, 5),
                MinMaxAccumulator.of(-10)
                        .merge(MinMaxAccumulator.of(5), comparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(-10, 10),
                MinMaxAccumulator.of(10)
                        .merge(MinMaxAccumulator.of(-10), comparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(Integer.MIN_VALUE, Integer.MAX_VALUE),
                MinMaxAccumulator.of(Integer.MIN_VALUE)
                        .merge(MinMaxAccumulator.of(Integer.MAX_VALUE), comparator)
                        .minMaxMaybe()
                        .orElseThrow());

        final Comparator<Integer> reverseComparator = Comparator.reverseOrder();
        Assertions.assertEquals(
                Optional.empty(),
                MinMaxAccumulator.<Integer>of(null)
                        .merge(MinMaxAccumulator.of(null), reverseComparator)
                        .minMaxMaybe());
        Assertions.assertEquals(
                new MinMax<>(2, 2),
                MinMaxAccumulator.of(2)
                        .merge(MinMaxAccumulator.of(null), reverseComparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(5, 5),
                MinMaxAccumulator.<Integer>of(null)
                        .merge(MinMaxAccumulator.of(5), reverseComparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(5, 4),
                MinMaxAccumulator.of(4)
                        .merge(MinMaxAccumulator.of(5), reverseComparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(5, 5),
                MinMaxAccumulator.of(5)
                        .merge(MinMaxAccumulator.of(5), reverseComparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(5, -10),
                MinMaxAccumulator.of(-10)
                        .merge(MinMaxAccumulator.of(5), reverseComparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(10, -10),
                MinMaxAccumulator.of(10)
                        .merge(MinMaxAccumulator.of(-10), reverseComparator)
                        .minMaxMaybe()
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(Integer.MAX_VALUE, Integer.MIN_VALUE),
                MinMaxAccumulator.of(Integer.MIN_VALUE)
                        .merge(MinMaxAccumulator.of(Integer.MAX_VALUE), reverseComparator)
                        .minMaxMaybe()
                        .orElseThrow());
    }
}
