package ch.uetlib.core.utils;

import static java.util.function.Function.identity;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ListUtilsTest {

    private final List<Pair<String, Integer>> pairs1 =
            List.of(Pair.of("A", 1), Pair.of("A", 2), Pair.of("B", 1), Pair.of("C", 1));
    private final List<Pair<String, Integer>> pairs2 =
            List.of(Pair.of("A", 1), Pair.of("B", 1), Pair.of("A", 3));

    @Test
    void removeAllIdentity() {
        Assertions.assertEquals(
                org.apache.commons.collections4.ListUtils.removeAll(pairs1, pairs2),
                ListUtils.removeAll(pairs1, pairs2, identity()));
        Assertions.assertEquals(
                ListUtils.removeAll(pairs1, pairs2, identity()),
                org.apache.commons.collections4.ListUtils.removeAll(pairs1, pairs2));

        Assertions.assertEquals(
                List.of(Pair.of("A", 2), Pair.of("C", 1)),
                ListUtils.removeAll(pairs1, pairs2, identity()));

        Assertions.assertEquals(
                List.of(Pair.of("A", 3)), ListUtils.removeAll(pairs2, pairs1, identity()));
    }

    @Test
    void removeAll() {
        Assertions.assertEquals(
                List.of(Pair.of("C", 1)), ListUtils.removeAll(pairs1, pairs2, Pair::getLeft));
        Assertions.assertEquals(List.of(), ListUtils.removeAll(pairs2, pairs1, Pair::getLeft));

        Assertions.assertEquals(
                List.of(Pair.of("A", 2)), ListUtils.removeAll(pairs1, pairs2, Pair::getRight));
        Assertions.assertEquals(
                List.of(Pair.of("A", 3)), ListUtils.removeAll(pairs2, pairs1, Pair::getRight));
    }

    @Test
    void removeAllParallelIdentity() {
        Assertions.assertEquals(
                org.apache.commons.collections4.ListUtils.removeAll(pairs1, pairs2),
                ListUtils.removeAllParallel(pairs1, pairs2, identity()));
        Assertions.assertEquals(
                ListUtils.removeAllParallel(pairs1, pairs2, identity()),
                org.apache.commons.collections4.ListUtils.removeAll(pairs1, pairs2));

        Assertions.assertEquals(
                List.of(Pair.of("A", 2), Pair.of("C", 1)),
                ListUtils.removeAllParallel(pairs1, pairs2, identity()));

        Assertions.assertEquals(
                List.of(Pair.of("A", 3)), ListUtils.removeAllParallel(pairs2, pairs1, identity()));
    }

    @Test
    void removeAllParallel() {
        Assertions.assertEquals(
                List.of(Pair.of("C", 1)),
                ListUtils.removeAllParallel(pairs1, pairs2, Pair::getLeft));
        Assertions.assertEquals(
                List.of(), ListUtils.removeAllParallel(pairs2, pairs1, Pair::getLeft));

        Assertions.assertEquals(
                List.of(Pair.of("A", 2)),
                ListUtils.removeAllParallel(pairs1, pairs2, Pair::getRight));
        Assertions.assertEquals(
                List.of(Pair.of("A", 3)),
                ListUtils.removeAllParallel(pairs2, pairs1, Pair::getRight));
    }
}
