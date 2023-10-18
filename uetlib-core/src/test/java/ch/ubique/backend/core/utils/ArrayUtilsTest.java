package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.ArrayUtils.lastElement;
import static ch.ubique.backend.core.utils.ArrayUtils.singletonOrNull;
import static org.junit.jupiter.api.Assertions.*;

import ch.ubique.backend.test.assertion.AssertionUtils;
import org.junit.jupiter.api.Test;

class ArrayUtilsTest {

    @Test
    void testLastElement() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> lastElement(new int[] {}));

        assertEquals('e', lastElement(new char[] {'a', 'b', 'c', 'd', 'e'}));
        assertEquals(5, lastElement(new int[] {1, 2, 3, 4, 5}));
        assertEquals(5L, lastElement(new long[] {1, 2, 3, 4, 5}));
        assertEquals(5.5, lastElement(new double[] {1.1, 2.2, 3.3, 4.4, 5.5}));
        assertEquals("5", lastElement(new String[] {"1", "2", "3", "4", "5"}));
    }

    @Test
    void testSingletonOrNull() {
        assertEquals(0, singletonOrNull(new char[] {}));
        assertEquals(0, singletonOrNull(new int[] {}));
        assertEquals(0, singletonOrNull(new long[] {}));
        assertEquals(0.0, singletonOrNull(new double[] {}));
        assertNull(singletonOrNull(new String[][] {}));

        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> singletonOrNull(new char[] {'a', 'b', 'c', 'd', 'e'}),
                "Can extract singleton only when array length == 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> singletonOrNull(new int[] {1, 2, 3, 4, 5}),
                "Can extract singleton only when array length == 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> singletonOrNull(new long[] {1, 2, 3, 4, 5}),
                "Can extract singleton only when array length == 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> singletonOrNull(new double[] {1.1, 2.2, 3.3, 4.4, 5.5}),
                "Can extract singleton only when array length == 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> singletonOrNull(new String[] {"1", "2", "3", "4", "5"}),
                "Can extract singleton only when array length == 1");

        assertEquals(
                'a',
                singletonOrNull(
                        new char[] {
                            'a',
                        }));
        assertEquals(
                1,
                singletonOrNull(
                        new int[] {
                            1,
                        }));
        assertEquals(1L, singletonOrNull(new long[] {1}));
        assertEquals(1.1, singletonOrNull(new double[] {1.1}));
        assertEquals("1", singletonOrNull(new String[] {"1"}));
    }
}
