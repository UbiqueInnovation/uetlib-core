package ch.ubique.backend.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class StringUtilsTest {

    @Test
    void substringWindows() {
        assertEquals(List.of(), StringUtils.substringWindows(null, 3).toList());
        assertEquals(List.of(), StringUtils.substringWindows("Te", 3).toList());
        assertEquals(List.of("Tes"), StringUtils.substringWindows("Tes", 3).toList());
        assertEquals(List.of(), StringUtils.substringWindows("Tes", -1).toList());
        assertEquals(
                List.of("Tes", "est", "stT", "tTe", "Tes", "est"),
                StringUtils.substringWindows("TestTest", 3).toList());
    }

    @Test
    void requireNonBlank() {
        assertThrows(NullPointerException.class, () -> StringUtils.requireNonBlank(null));
        assertThrows(IllegalStateException.class, () -> StringUtils.requireNonBlank(""));
        assertThrows(IllegalStateException.class, () -> StringUtils.requireNonBlank("    "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlank(" a "));
    }

    @Test
    void requireNonBlankElse() {
        assertThrows(NullPointerException.class, () -> StringUtils.requireNonBlankElse(null, null));
        assertThrows(NullPointerException.class, () -> StringUtils.requireNonBlankElse("", null));
        assertThrows(
                NullPointerException.class, () -> StringUtils.requireNonBlankElse("    ", null));
        assertThrows(
                IllegalArgumentException.class, () -> StringUtils.requireNonBlankElse(null, ""));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.requireNonBlankElse("", ""));
        assertThrows(
                IllegalArgumentException.class, () -> StringUtils.requireNonBlankElse("    ", ""));
        assertThrows(
                IllegalArgumentException.class, () -> StringUtils.requireNonBlankElse(null, "\t"));
        assertThrows(
                IllegalArgumentException.class, () -> StringUtils.requireNonBlankElse("", "\t"));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse("    ", "\t"));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse("    ", "\n"));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("abc", null));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("abc", ""));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("abc", "    "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse(null, " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("", " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("\t", " a "));
    }

    @Test
    void requireNonBlankElseGet() {
        assertThrows(
                NullPointerException.class, () -> StringUtils.requireNonBlankElseGet(null, null));
        assertThrows(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> null));
        assertThrows(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> null));
        assertThrows(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> null));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> ""));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> ""));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> ""));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> "\t"));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> "\t"));
        assertThrows(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> "\t"));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("abc", null));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("abc", () -> null));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("abc", () -> " "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet(null, () -> " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("", () -> " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("\t", () -> " a "));
    }

    @Test
    void normalizeSpaceAndNonBreakingSpace() {
        assertNull(StringUtils.normalizeSpaceAndNonBreakingSpace(null));

        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace(""));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace(" "));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace("\t"));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace("\n"));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace("\r"));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace(" "));

        assertEquals("..", StringUtils.normalizeSpaceAndNonBreakingSpace(".."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".  ."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".\t\t."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".\n\n."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".\r\r."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".  ."));

        assertEquals("..", StringUtils.normalizeSpaceAndNonBreakingSpace(".."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(" .  . "));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace("\t.\t\t.\t"));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace("\n.\n\n.\n"));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace("\r.\r\r.\r"));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(" .  . "));
    }

    @Test
    void normalizeAndStripToNull() {
        assertNull(StringUtils.normalizeAndStripToNull(null));

        assertNull(StringUtils.normalizeAndStripToNull(""));
        assertNull(StringUtils.normalizeAndStripToNull(" "));
        assertNull(StringUtils.normalizeAndStripToNull("\t"));
        assertNull(StringUtils.normalizeAndStripToNull("\n"));
        assertNull(StringUtils.normalizeAndStripToNull("\r"));
        assertNull(StringUtils.normalizeAndStripToNull(" "));

        assertEquals("..", StringUtils.normalizeAndStripToNull(".."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".  ."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".\t\t."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".\n\n."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".\r\r."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".  ."));

        assertEquals("..", StringUtils.normalizeAndStripToNull(".."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(" .  . "));
        assertEquals(". .", StringUtils.normalizeAndStripToNull("\t.\t\t.\t"));
        assertEquals(". .", StringUtils.normalizeAndStripToNull("\n.\n\n.\n"));
        assertEquals(". .", StringUtils.normalizeAndStripToNull("\r.\r\r.\r"));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(" .  . "));
    }
}
