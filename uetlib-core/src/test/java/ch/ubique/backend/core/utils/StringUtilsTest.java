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

    @Test
    void requireNonBlank() {
        Assertions.assertThrows(
                NullPointerException.class, () -> StringUtils.requireNonBlank(null));
        Assertions.assertThrows(IllegalStateException.class, () -> StringUtils.requireNonBlank(""));
        Assertions.assertThrows(
                IllegalStateException.class, () -> StringUtils.requireNonBlank("    "));
        Assertions.assertDoesNotThrow(() -> StringUtils.requireNonBlank(" a "));
    }

    @Test
    void requireNonBlankElse() {
        Assertions.assertThrows(
                NullPointerException.class, () -> StringUtils.requireNonBlankElse(null, null));
        Assertions.assertThrows(
                NullPointerException.class, () -> StringUtils.requireNonBlankElse("", null));
        Assertions.assertThrows(
                NullPointerException.class, () -> StringUtils.requireNonBlankElse("    ", null));
        Assertions.assertThrows(
                IllegalStateException.class, () -> StringUtils.requireNonBlankElse(null, ""));
        Assertions.assertThrows(
                IllegalStateException.class, () -> StringUtils.requireNonBlankElse("", ""));
        Assertions.assertThrows(
                IllegalStateException.class, () -> StringUtils.requireNonBlankElse("    ", ""));
        Assertions.assertThrows(
                IllegalStateException.class, () -> StringUtils.requireNonBlankElse(null, "   "));
        Assertions.assertThrows(
                IllegalStateException.class, () -> StringUtils.requireNonBlankElse("", "   "));
        Assertions.assertThrows(
                IllegalStateException.class, () -> StringUtils.requireNonBlankElse("    ", "   "));
        Assertions.assertDoesNotThrow(() -> StringUtils.requireNonBlankElse(null, " a "));
        Assertions.assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("", " a "));
        Assertions.assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("   ", " a "));
    }

    @Test
    void requireNonBlankElseGet() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> null));
        Assertions.assertThrows(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> null));
        Assertions.assertThrows(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> null));
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> ""));
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> ""));
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> ""));
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> "   "));
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> "   "));
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> "   "));
        Assertions.assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet(null, () -> " a "));
        Assertions.assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("", () -> " a "));
        Assertions.assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("   ", () -> " a "));
    }
}
