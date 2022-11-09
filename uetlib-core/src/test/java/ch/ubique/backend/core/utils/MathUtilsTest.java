package ch.ubique.backend.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MathUtilsTest {

    @Test
    void testClamp() {
        Assertions.assertEquals(5, MathUtils.clamp(5, 0, 10));
        Assertions.assertEquals(0, MathUtils.clamp(-5, 0, 10));
        Assertions.assertEquals(10, MathUtils.clamp(15, 0, 10));
    }
}
