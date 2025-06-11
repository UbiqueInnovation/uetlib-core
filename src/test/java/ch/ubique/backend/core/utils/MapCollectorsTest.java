package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.function.Function.identity;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class MapCollectorsTest {

    @Test
    void entriesToMap() {
        final var map = ofEntries(entry("a", 1), entry("b", 2), entry("c", 3));
        final var entries = map.entrySet();
        final var pairs = entries.stream().map(Pair::of).toList();
        assertEquals(map, entries.stream().collect(MapCollectors.entriesToMap()));
        assertEquals(map, pairs.stream().collect(MapCollectors.entriesToMap()));
    }

    @Test
    void entriesToMapWithValueMapper() {
        final var map = ofEntries(entry("a", 1), entry("b", 2), entry("c", 3));
        final var expectedMap = ofEntries(entry("a", "100"), entry("b", "200"), entry("c", "300"));
        assertEquals(
                expectedMap,
                map.entrySet().stream()
                        .collect(
                                MapCollectors.entriesToMapWithValueMapper(
                                        v -> String.valueOf(100 * v))));
    }

    @Test
    void entriesToMapWithMerge() {
        final var entries = Set.of(entry("a", 1), entry("a", 2), entry("b", 4), entry("c", 5));
        final var expected = ofEntries(entry("a", 3), entry("b", 4), entry("c", 5));
        final var pairs = entries.stream().map(Pair::of).toList();
        assertEquals(expected, entries.stream().collect(MapCollectors.entriesToMap(Integer::sum)));
        assertEquals(expected, pairs.stream().collect(MapCollectors.entriesToMap(Integer::sum)));
    }

    @Test
    void entriesToInvertedMap() {
        final var map = ofEntries(entry("a", 1), entry("b", 2), entry("c", 3));
        final var invertedMap = MapUtils.invertMap(map);
        final var entries = map.entrySet();
        final var pairs = entries.stream().map(Pair::of).toList();
        assertEquals(invertedMap, entries.stream().collect(MapCollectors.entriesToInvertedMap()));
        assertEquals(invertedMap, pairs.stream().collect(MapCollectors.entriesToInvertedMap()));
    }

    @Test
    void entriesToInvertedMapWithMerge() {
        final var entries =
                Set.of(entry(Set.of("a"), 1), entry(Set.of("b"), 1), entry(Set.of("c"), 2));
        final var expected = ofEntries(entry(1, Set.of("a", "b")), entry(2, Set.of("c")));
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
        final var map = ofEntries(entry(1, Pair.of(1, "a")), entry(2, Pair.of(2, "b")));
        assertEquals(map, map.values().stream().collect(MapCollectors.toMap(Pair::getLeft)));
    }

    @Test
    void valueToMap() {
        final var map = Map.of(1, 1, 2, 2, 3, 3);
        assertEquals(
                map, map.keySet().stream().collect(MapCollectors.toMapWithValueMapper(identity())));
    }

    @Test
    void groupingEntriesByKey() {
        final var entries = List.of(entry("a", 1), entry("a", 2), entry("b", 4), entry("c", 5));
        final var expected =
                ofEntries(
                        entry("a", List.of(1, 2)), entry("b", List.of(4)), entry("c", List.of(5)));
        assertEquals(expected, entries.stream().collect(MapCollectors.groupingEntriesByKey()));
        assertEquals(
                expected,
                entries.stream()
                        .collect(
                                MapCollectors.groupingByAndMapValues(
                                        Entry::getKey, Entry::getValue)));
    }

    @Test
    void groupingByAndMapValues() {
        final var entries = List.of(entry("a", 1), entry("a", 2), entry("b", 4), entry("c", 5));
        final var expected = ofEntries(entry(0, List.of(-4)), entry(1, List.of(-1, -2, -5)));
        assertEquals(
                expected,
                entries.stream()
                        .collect(
                                MapCollectors.groupingByAndMapValues(
                                        e -> e.getKey().charAt(0) % 2, e -> -e.getValue())));
    }
}
