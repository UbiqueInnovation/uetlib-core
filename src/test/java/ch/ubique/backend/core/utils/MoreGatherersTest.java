package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MoreGatherersTest {
    @Test
    void testDistinctGatherer() {
        final var list = new ArrayList<Integer>();
        IntStream.range(0, 10)
                .mapToObj(i -> i / 2)
                .gather(MoreGatherers.distinctBy(Function.identity()))
                .forEach(list::add);
        assertEquals(List.of(0, 1, 2, 3, 4), list);

        final var arrays =
                List.of(
                        new int[] {1, 2, 3, 4},
                        new int[] {1, 3, 5, 7},
                        new int[] {2, 3, 4, 5},
                        new int[] {2, 3, 4, 6});
        final var list0 = new ArrayList<int[]>();
        final var list1 = new ArrayList<int[]>();
        final var list2 = new ArrayList<int[]>();
        final var list3 = new ArrayList<int[]>();
        arrays.stream().gather(MoreGatherers.distinctBy(a -> a[0])).forEach(list0::add);
        arrays.stream().gather(MoreGatherers.distinctBy(a -> a[1])).forEach(list1::add);
        arrays.stream().gather(MoreGatherers.distinctBy(a -> a[2])).forEach(list2::add);
        arrays.stream().gather(MoreGatherers.distinctBy(a -> a[3])).forEach(list3::add);

        assertEquals(List.of(new int[] {1, 2, 3, 4}, new int[] {2, 3, 4, 5}), list0);
        assertEquals(List.of(new int[] {1, 2, 3, 4}, new int[] {1, 3, 5, 7}), list1);
        assertEquals(
                List.of(new int[] {1, 2, 3, 4}, new int[] {1, 3, 5, 7}, new int[] {2, 3, 4, 5}),
                list2);
        assertEquals(
                List.of(
                        new int[] {1, 2, 3, 4},
                        new int[] {1, 3, 5, 7},
                        new int[] {2, 3, 4, 5},
                        new int[] {2, 3, 4, 6}),
                list3);
    }
}
