package ch.ubique.uetlib.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

class MapUtilsTest {

    @Test
    void testSorting() {
        final var map =
                Map.ofEntries(
                        Map.entry(5, new int[] {1}),
                        Map.entry(3, new int[] {2}),
                        Map.entry(4, new int[] {3}),
                        Map.entry(2, new int[] {4}),
                        Map.entry(6, new int[] {5}),
                        Map.entry(1, new int[] {6}));
        final var sorted = List.of(6, 4, 2, 3, 1, 5);
        final var sortedResult = MapUtils.sortMapByKeys(map);
        assertEquals(sorted.size(), sortedResult.size());
        final var sortedIt = sorted.iterator();
        final var sortedResultIt = sortedResult.iterator();

        while (sortedIt.hasNext() && sortedResultIt.hasNext()) {
            assertEquals(sortedIt.next(), sortedResultIt.next()[0]);
        }
        assertFalse(sortedIt.hasNext());
    }

    @Test
    void testNonNullKeyValueOrNull() {
        // Case 1: Both key and value are non-null
        final var entry = new AbstractMap.SimpleEntry<>("key", "value");
        final var result = MapUtils.nonNullKeyValueOrNull(entry);
        assertNotNull(result);
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());

        // Case 2: Key is null
        final var entryWithNullKey = new AbstractMap.SimpleEntry<>(null, "value");
        assertNull(MapUtils.nonNullKeyValueOrNull(entryWithNullKey));

        // Case 3: Value is null
        final var entryWithNullValue = new AbstractMap.SimpleEntry<>("key", null);
        assertNull(MapUtils.nonNullKeyValueOrNull(entryWithNullValue));

        // Case 4: Both are null
        final var entryWithBothNull = new AbstractMap.SimpleEntry<>(null, null);
        assertNull(MapUtils.nonNullKeyValueOrNull(entryWithBothNull));
    }

    @Test
    void testNonNullKeyValueOrNullWithComponents() {
        // Case 1: Both key and value are non-null
        final var result = MapUtils.nonNullKeyValueOrNull("key", "value");
        assertNotNull(result);
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());

        // Case 2: Key is null
        assertNull(MapUtils.nonNullKeyValueOrNull(null, "value"));

        // Case 3: Value is null
        assertNull(MapUtils.nonNullKeyValueOrNull("key", null));

        // Case 4: Both are null
        assertNull(MapUtils.nonNullKeyValueOrNull(null, null));
    }
}
