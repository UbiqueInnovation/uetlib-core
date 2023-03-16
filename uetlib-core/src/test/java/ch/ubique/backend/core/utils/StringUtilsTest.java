package ch.ubique.backend.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StringUtilsTest {

    @Test
    void substringWindows() {
        Assertions.assertEquals(List.of(), StringUtils.substringWindows(null, 3).toList());
        Assertions.assertEquals(List.of(), StringUtils.substringWindows("Te", 3).toList());
        Assertions.assertEquals(List.of("Tes"), StringUtils.substringWindows("Tes", 3).toList());
        Assertions.assertEquals(List.of(), StringUtils.substringWindows("Tes", -1).toList());
        Assertions.assertEquals(
                List.of("Tes", "est", "stT", "tTe", "Tes", "est"),
                StringUtils.substringWindows("TestTest", 3).toList());
    }
}
