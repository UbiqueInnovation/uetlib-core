package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

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
}
