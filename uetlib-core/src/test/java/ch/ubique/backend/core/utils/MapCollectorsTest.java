package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

class MapCollectorsTest {

    @Test
    void entriesToMap() {
        final var map = Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2), Map.entry("c", 3));
        final var entries = map.entrySet();
        final var pairs = entries.stream().map(Pair::of).toList();
        assertEquals(map, entries.stream().collect(MapCollectors.entriesToMap()));
        assertEquals(map, pairs.stream().collect(MapCollectors.entriesToMap()));
    }

    @Test
    void entriesToMapWithMerge() {
        final var entries =
                Set.of(Map.entry("a", 1), Map.entry("a", 2), Map.entry("b", 4), Map.entry("c", 5));
        final var expected = Map.ofEntries(Map.entry("a", 3), Map.entry("b", 4), Map.entry("c", 5));
        final var pairs = entries.stream().map(Pair::of).toList();
        assertEquals(expected, entries.stream().collect(MapCollectors.entriesToMap(Integer::sum)));
        assertEquals(expected, pairs.stream().collect(MapCollectors.entriesToMap(Integer::sum)));
    }

    @Test
    void entriesToInvertedMap() {
        final var map = Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2), Map.entry("c", 3));
        final var invertedMap = MapUtils.invertMap(map);
        final var entries = map.entrySet();
        final var pairs = entries.stream().map(Pair::of).toList();
        assertEquals(invertedMap, entries.stream().collect(MapCollectors.entriesToInvertedMap()));
        assertEquals(invertedMap, pairs.stream().collect(MapCollectors.entriesToInvertedMap()));
    }

    @Test
    void entriesToInvertedMapWithMerge() {
        final var entries =
                Set.of(
                        Map.entry(Set.of("a"), 1),
                        Map.entry(Set.of("b"), 1),
                        Map.entry(Set.of("c"), 2));
        final var expected =
                Map.ofEntries(Map.entry(1, Set.of("a", "b")), Map.entry(2, Set.of("c")));
        final var pairs = entries.stream().map(Pair::of).toList();
        assertEquals(
                expected,
                entries.stream().collect(MapCollectors.entriesToInvertedMap(SetUtils::union)));
        assertEquals(
                expected,
                pairs.stream().collect(MapCollectors.entriesToInvertedMap(SetUtils::union)));
    }

    @Test
    void toMap() {
        final var map = Map.ofEntries(Map.entry(1, Pair.of(1, "a")), Map.entry(2, Pair.of(2, "b")));
        assertEquals(map, map.values().stream().collect(MapCollectors.toMap(Pair::getLeft)));
    }

    @Test
    void valueToMap() {
        final var map = Map.of(1, 1, 2, 2, 3, 3);
        assertEquals(
                map,
                map.keySet().stream()
                        .collect(MapCollectors.toMapWithValueMapper(Function.identity())));
    }
}
