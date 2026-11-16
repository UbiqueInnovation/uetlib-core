package ch.uetlib.core.utils;

import static ch.uetlib.core.utils.ArrayUtils.lastElement;
import static ch.uetlib.core.utils.ArrayUtils.singletonOrNull;

import static org.junit.jupiter.api.Assertions.*;

import ch.uetlib.test.assertion.AssertionUtils;

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
    void testSingletonOrNullOrZero() {
        assertEquals(0, ArrayUtils.singletonOrZero(new char[] {}));
        assertEquals(0, ArrayUtils.singletonOrZero(new int[] {}));
        assertEquals(0, ArrayUtils.singletonOrZero(new long[] {}));
        assertEquals(0.0, ArrayUtils.singletonOrZero(new double[] {}));
        assertNull(singletonOrNull(new String[][] {}));

        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> ArrayUtils.singletonOrZero(new char[] {'a', 'b', 'c', 'd', 'e'}),
                "Cannot extract singleton when array length > 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> ArrayUtils.singletonOrZero(new int[] {1, 2, 3, 4, 5}),
                "Cannot extract singleton when array length > 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> ArrayUtils.singletonOrZero(new long[] {1, 2, 3, 4, 5}),
                "Cannot extract singleton when array length > 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> ArrayUtils.singletonOrZero(new double[] {1.1, 2.2, 3.3, 4.4, 5.5}),
                "Cannot extract singleton when array length > 1");
        AssertionUtils.assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> singletonOrNull(new String[] {"1", "2", "3", "4", "5"}),
                "Cannot extract singleton when array length > 1");

        assertEquals(
                'a',
                ArrayUtils.singletonOrZero(
                        new char[] {
                            'a',
                        }));
        assertEquals(
                1,
                ArrayUtils.singletonOrZero(
                        new int[] {
                            1,
                        }));
        assertEquals(1L, ArrayUtils.singletonOrZero(new long[] {1}));
        assertEquals(1.1, ArrayUtils.singletonOrZero(new double[] {1.1}));
        assertEquals("1", singletonOrNull(new String[] {"1"}));
    }
}
