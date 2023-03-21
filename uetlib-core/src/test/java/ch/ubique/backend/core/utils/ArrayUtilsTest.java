package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ArrayUtilsTest {

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void lastElement() {
        assertThrows(
                ArrayIndexOutOfBoundsException.class, () -> ArrayUtils.lastElement(new int[] {}));

        assertEquals('e', ArrayUtils.lastElement(new char[] {'a', 'b', 'c', 'd', 'e'}));
        assertEquals(5, ArrayUtils.lastElement(new int[] {1, 2, 3, 4, 5}));
        assertEquals(5L, ArrayUtils.lastElement(new long[] {1, 2, 3, 4, 5}));
        assertEquals(5.5, ArrayUtils.lastElement(new double[] {1.1, 2.2, 3.3, 4.4, 5.5}));
        assertEquals("5", ArrayUtils.lastElement(new String[] {"1", "2", "3", "4", "5"}));
    }
}
