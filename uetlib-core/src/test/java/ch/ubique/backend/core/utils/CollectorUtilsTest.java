package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.utils.CollectorUtils.MinMax;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CollectorUtilsTest {

    @Test
    @SuppressWarnings("java:S5778")
    void testCollectMinMax() {
        //noinspection ResultOfMethodCallIgnored
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> Stream.<Integer>of().collect(CollectorUtils.collectMinMax()));
        Assertions.assertEquals(
                new MinMax<>(56, 56), Stream.of(56).collect(CollectorUtils.collectMinMax()));
        Assertions.assertEquals(
                new MinMax<>(56, 56), Stream.of(56, 56).collect(CollectorUtils.collectMinMax()));
        Assertions.assertEquals(
                new MinMax<>(56, 57), Stream.of(56, 57).collect(CollectorUtils.collectMinMax()));
        Assertions.assertEquals(
                new MinMax<>(-2, 869),
                Stream.of(56, 57, -2, 869, 0).collect(CollectorUtils.collectMinMax()));
        Assertions.assertEquals(
                new MinMax<>(Integer.MIN_VALUE, Integer.MAX_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(CollectorUtils.collectMinMax()));

        //noinspection ResultOfMethodCallIgnored
        Assertions.assertThrows(
                NoSuchElementException.class,
                () ->
                        Stream.<Integer>of()
                                .collect(
                                        CollectorUtils.collectMinMax(
                                                Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56)
                        .collect(CollectorUtils.collectMinMax(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56)
                        .collect(CollectorUtils.collectMinMax(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(57, 56),
                Stream.of(56, 57)
                        .collect(CollectorUtils.collectMinMax(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(869, -2),
                Stream.of(56, 57, -2, 869, 0)
                        .collect(CollectorUtils.collectMinMax(Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(Integer.MAX_VALUE, Integer.MIN_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(CollectorUtils.collectMinMax(Comparator.<Integer>reverseOrder())));
    }

    @Test
    void testCollectMinMaxOptional() {
        Assertions.assertEquals(
                Optional.empty(),
                Stream.<Integer>of().collect(CollectorUtils.collectMinMaxOptional()));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56).collect(CollectorUtils.collectMinMaxOptional()).orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56).collect(CollectorUtils.collectMinMaxOptional()).orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(56, 57),
                Stream.of(56, 57).collect(CollectorUtils.collectMinMaxOptional()).orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(-2, 869),
                Stream.of(56, 57, -2, 869, 0)
                        .collect(CollectorUtils.collectMinMaxOptional())
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(Integer.MIN_VALUE, Integer.MAX_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(CollectorUtils.collectMinMaxOptional())
                        .orElseThrow());

        Assertions.assertEquals(
                Optional.empty(),
                Stream.<Integer>of()
                        .collect(
                                CollectorUtils.collectMinMaxOptional(
                                        Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56)
                        .collect(
                                CollectorUtils.collectMinMaxOptional(
                                        Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56)
                        .collect(
                                CollectorUtils.collectMinMaxOptional(
                                        Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(57, 56),
                Stream.of(56, 57)
                        .collect(
                                CollectorUtils.collectMinMaxOptional(
                                        Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(869, -2),
                Stream.of(56, 57, -2, 869, 0)
                        .collect(
                                CollectorUtils.collectMinMaxOptional(
                                        Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
        Assertions.assertEquals(
                new MinMax<>(Integer.MAX_VALUE, Integer.MIN_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(
                                CollectorUtils.collectMinMaxOptional(
                                        Comparator.<Integer>reverseOrder()))
                        .orElseThrow());
    }

    @Test
    @SuppressWarnings("java:S5778")
    void testCollectMinMaxNullable() {
        Assertions.assertEquals(
                new MinMax<>(null, null),
                Stream.<Integer>of().collect(CollectorUtils.collectMinMaxNullable()));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56).collect(CollectorUtils.collectMinMaxNullable()));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56).collect(CollectorUtils.collectMinMaxNullable()));
        Assertions.assertEquals(
                new MinMax<>(56, 57),
                Stream.of(56, 57).collect(CollectorUtils.collectMinMaxNullable()));
        Assertions.assertEquals(
                new MinMax<>(-2, 869),
                Stream.of(56, 57, -2, 869, 0).collect(CollectorUtils.collectMinMaxNullable()));
        Assertions.assertEquals(
                new MinMax<>(Integer.MIN_VALUE, Integer.MAX_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(CollectorUtils.collectMinMaxNullable()));

        Assertions.assertEquals(
                new MinMax<>(null, null),
                Stream.<Integer>of()
                        .collect(
                                CollectorUtils.collectMinMaxNullable(
                                        Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56)
                        .collect(
                                CollectorUtils.collectMinMaxNullable(
                                        Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(56, 56),
                Stream.of(56, 56)
                        .collect(
                                CollectorUtils.collectMinMaxNullable(
                                        Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(57, 56),
                Stream.of(56, 57)
                        .collect(
                                CollectorUtils.collectMinMaxNullable(
                                        Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(869, -2),
                Stream.of(56, 57, -2, 869, 0)
                        .collect(
                                CollectorUtils.collectMinMaxNullable(
                                        Comparator.<Integer>reverseOrder())));
        Assertions.assertEquals(
                new MinMax<>(Integer.MAX_VALUE, Integer.MIN_VALUE),
                Stream.of(56, 57, -2, 869, 0, Integer.MAX_VALUE, Integer.MIN_VALUE)
                        .collect(
                                CollectorUtils.collectMinMaxNullable(
                                        Comparator.<Integer>reverseOrder())));
    }

    @Test
    void testMinNullable() {
        Assertions.assertNull(CollectorUtils.minNullable(null, null));
        Assertions.assertEquals(2, CollectorUtils.minNullable(2, null));
        Assertions.assertEquals(5, CollectorUtils.minNullable(null, 5));
        Assertions.assertEquals(4, CollectorUtils.minNullable(4, 5));
        Assertions.assertEquals(5, CollectorUtils.minNullable(5, 5));
        Assertions.assertEquals(-10, CollectorUtils.minNullable(-10, 5));
        Assertions.assertEquals(-10, CollectorUtils.minNullable(10, -10));
        Assertions.assertEquals(
                Integer.MIN_VALUE,
                CollectorUtils.minNullable(Integer.MIN_VALUE, Integer.MAX_VALUE));

        Assertions.assertNull(
                CollectorUtils.minNullable(null, null, Comparator.<Integer>reverseOrder()));
        Assertions.assertEquals(2, CollectorUtils.minNullable(2, null, Comparator.reverseOrder()));
        Assertions.assertEquals(5, CollectorUtils.minNullable(null, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, CollectorUtils.minNullable(4, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, CollectorUtils.minNullable(5, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, CollectorUtils.minNullable(-10, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(10, CollectorUtils.minNullable(10, -10, Comparator.reverseOrder()));
        Assertions.assertEquals(
                Integer.MAX_VALUE,
                CollectorUtils.minNullable(
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Comparator.reverseOrder()));
    }

    @Test
    void testMaxNullable() {
        Assertions.assertNull(CollectorUtils.maxNullable(null, null));
        Assertions.assertEquals(2, CollectorUtils.maxNullable(2, null));
        Assertions.assertEquals(5, CollectorUtils.maxNullable(null, 5));
        Assertions.assertEquals(5, CollectorUtils.maxNullable(4, 5));
        Assertions.assertEquals(5, CollectorUtils.maxNullable(5, 5));
        Assertions.assertEquals(5, CollectorUtils.maxNullable(-10, 5));
        Assertions.assertEquals(10, CollectorUtils.maxNullable(10, -10));
        Assertions.assertEquals(
                Integer.MAX_VALUE,
                CollectorUtils.maxNullable(Integer.MIN_VALUE, Integer.MAX_VALUE));

        Assertions.assertNull(
                CollectorUtils.maxNullable(null, null, Comparator.<Integer>reverseOrder()));
        Assertions.assertEquals(2, CollectorUtils.maxNullable(2, null, Comparator.reverseOrder()));
        Assertions.assertEquals(5, CollectorUtils.maxNullable(null, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(4, CollectorUtils.maxNullable(4, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(5, CollectorUtils.maxNullable(5, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(-10, CollectorUtils.maxNullable(-10, 5, Comparator.reverseOrder()));
        Assertions.assertEquals(
                -10, CollectorUtils.maxNullable(10, -10, Comparator.reverseOrder()));
        Assertions.assertEquals(
                Integer.MIN_VALUE,
                CollectorUtils.maxNullable(
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Comparator.reverseOrder()));
    }
}
