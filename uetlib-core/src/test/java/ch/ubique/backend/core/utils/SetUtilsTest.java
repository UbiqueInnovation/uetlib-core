package ch.ubique.backend.core.utils;

import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SetUtilsTest {

    private final Set<Pair<String, Integer>> pairs1 =
            Set.of(Pair.of("A", 1), Pair.of("A", 2), Pair.of("B", 1), Pair.of("C", 1));
    private final Set<Pair<String, Integer>> pairs2 =
            Set.of(Pair.of("A", 1), Pair.of("B", 1), Pair.of("A", 3));

    @Test
    void removeAllIdentity() {
        Assertions.assertEquals(
                org.apache.commons.collections4.SetUtils.difference(pairs1, pairs2),
                SetUtils.difference(pairs1, pairs2, Function.identity()));
        Assertions.assertEquals(
                SetUtils.difference(pairs1, pairs2, Function.identity()),
                org.apache.commons.collections4.SetUtils.difference(pairs1, pairs2));

        Assertions.assertEquals(
                Set.of(Pair.of("A", 2), Pair.of("C", 1)),
                SetUtils.difference(pairs1, pairs2, Function.identity()));

        Assertions.assertEquals(
                Set.of(Pair.of("A", 3)), SetUtils.difference(pairs2, pairs1, Function.identity()));
    }

    @Test
    void difference() {
        Assertions.assertEquals(
                Set.of(Pair.of("C", 1)), SetUtils.difference(pairs1, pairs2, Pair::getLeft));
        Assertions.assertEquals(Set.of(), SetUtils.difference(pairs2, pairs1, Pair::getLeft));

        Assertions.assertEquals(
                Set.of(Pair.of("A", 2)), SetUtils.difference(pairs1, pairs2, Pair::getRight));
        Assertions.assertEquals(
                Set.of(Pair.of("A", 3)), SetUtils.difference(pairs2, pairs1, Pair::getRight));
    }
}
