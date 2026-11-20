package ch.uetlib.core.utils;

import static ch.uetlib.test.assertion.AssertionUtils.assertThrowsWithMessage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

@SuppressWarnings("DataFlowIssue")
class StringUtilsTest {

    @Test
    void testSubstringWindows_ValidInputs_ShouldReturnExpectedValues() {
        assertEquals(List.of(), StringUtils.substringWindows(null, 3).toList());
        assertEquals(List.of(), StringUtils.substringWindows("Te", 3).toList());
        assertEquals(List.of("Tes"), StringUtils.substringWindows("Tes", 3).toList());
        assertEquals(List.of(), StringUtils.substringWindows("Tes", -1).toList());
        assertEquals(
                List.of("Tes", "est", "stT", "tTe", "Tes", "est"),
                StringUtils.substringWindows("TestTest", 3).toList());
    }

    @Test
    void testRequireNonBlank_NullValue_ShouldThrowException() {
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlank(null),
                "value must be non-blank");
    }

    @Test
    void testRequireNonBlank_BlankValue_ShouldThrowException() {
        assertThrowsWithMessage(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlank(""),
                "value must be non-blank");
        assertThrowsWithMessage(
                IllegalStateException.class,
                () -> StringUtils.requireNonBlank("    "),
                "value must be non-blank");
    }

    @Test
    void testRequireNonBlank_NonBlankValue_ShouldNotThrow() {
        assertDoesNotThrow(() -> StringUtils.requireNonBlank(" a "));
    }

    @Test
    void testRequireNonBlankElse_NullDefaultValue_ShouldThrowException() {
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElse(null, null),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElse("", null),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElse("    ", null),
                "defaultValue must be non-blank");
    }

    @Test
    void testRequireNonBlankElse_BlankDefaultValue_ShouldThrowException() {
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse(null, ""),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse("", ""),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse("    ", ""),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse(null, "\t"),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse("", "\t"),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse("    ", "\t"),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElse("    ", "\n"),
                "defaultValue must be non-blank");
    }

    @Test
    void testRequireNonBlankElse_NonBlankValue_ShouldNotThrow() {
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("abc", null));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("abc", ""));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("abc", "    "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse(null, " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("", " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElse("\t", " a "));
    }

    @Test
    void testRequireNonBlankWithMessage_NullMessage_ShouldThrowException() {
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlank(null, (String) null),
                "value must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlank("", (String) null),
                "value must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlank("    ", (String) null),
                "value must be non-blank");
    }

    @Test
    void testRequireNonBlankWithMessage_CustomMessage_ShouldThrowException() {
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlank("", "Custom message"),
                "Custom message");
    }

    @Test
    void testRequireNonBlankWithMessage_NonBlankValue_ShouldNotThrow() {
        assertDoesNotThrow(() -> StringUtils.requireNonBlank(" a ", (String) null));
    }

    @Test
    void testRequireNonBlankWithMessageSupplier_NullSupplier_ShouldThrowException() {
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlank(null, (java.util.function.Supplier<String>) null),
                "value must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlank("", (java.util.function.Supplier<String>) null),
                "value must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () ->
                        StringUtils.requireNonBlank(
                                "    ", (java.util.function.Supplier<String>) null),
                "value must be non-blank");
    }

    @Test
    void testRequireNonBlankWithMessageSupplier_CustomMessage_ShouldThrowException() {
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlank("", () -> "Custom supplier message"),
                "Custom supplier message");
    }

    @Test
    void testRequireNonBlankWithMessageSupplier_NonBlankValue_ShouldNotThrow() {
        assertDoesNotThrow(
                () ->
                        StringUtils.requireNonBlank(
                                " a ", (java.util.function.Supplier<String>) null));
    }

    @Test
    void testRequireNonBlankElseGet_NullSupplier_ShouldThrowException() {
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet(null, null),
                "defaultValueSupplier must not be null");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> null),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> null),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> null),
                "defaultValue must be non-blank");
    }

    @Test
    void testRequireNonBlankElseGet_BlankSupplierValue_ShouldThrowException() {
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> StringUtils.requireNonBlankElseGet(null, null),
                "defaultValueSupplier must not be null");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> ""),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> ""),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> ""),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet(null, () -> "\t"),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("", () -> "\t"),
                "defaultValue must be non-blank");
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> StringUtils.requireNonBlankElseGet("    ", () -> "\t"),
                "defaultValue must be non-blank");
    }

    @Test
    void testRequireNonBlankElseGet_NonBlankValue_ShouldNotThrow() {
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("abc", null));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("abc", () -> null));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("abc", () -> " "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet(null, () -> " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("", () -> " a "));
        assertDoesNotThrow(() -> StringUtils.requireNonBlankElseGet("\t", () -> " a "));
    }

    @Test
    void testNormalizeSpaceAndNonBreakingSpace_ShouldNormalizeExpectedValues() {
        assertNull(StringUtils.normalizeSpaceAndNonBreakingSpace(null));

        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace(""));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace(" "));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace("\t"));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace("\n"));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace("\r"));
        assertEquals("", StringUtils.normalizeSpaceAndNonBreakingSpace("\u00A0"));

        assertEquals("..", StringUtils.normalizeSpaceAndNonBreakingSpace(".."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".  ."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".\t\t."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".\n\n."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".\r\r."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(".\u00A0\u00A0."));

        assertEquals("..", StringUtils.normalizeSpaceAndNonBreakingSpace(".."));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace(" .  . "));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace("\t.\t\t.\t"));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace("\n.\n\n.\n"));
        assertEquals(". .", StringUtils.normalizeSpaceAndNonBreakingSpace("\r.\r\r.\r"));
        assertEquals(
                ". .", StringUtils.normalizeSpaceAndNonBreakingSpace("\u00A0.\u00A0\u00A0.\u00A0"));
    }

    @Test
    void testNormalizeAndStripToNull_ShouldNormalizeExpectedValues() {
        assertNull(StringUtils.normalizeAndStripToNull(null));

        assertNull(StringUtils.normalizeAndStripToNull(""));
        assertNull(StringUtils.normalizeAndStripToNull(" "));
        assertNull(StringUtils.normalizeAndStripToNull("\t"));
        assertNull(StringUtils.normalizeAndStripToNull("\n"));
        assertNull(StringUtils.normalizeAndStripToNull("\r"));
        assertNull(StringUtils.normalizeAndStripToNull("\u00A0"));

        assertEquals("..", StringUtils.normalizeAndStripToNull(".."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".  ."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".\t\t."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".\n\n."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".\r\r."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(".\u00A0\u00A0."));

        assertEquals("..", StringUtils.normalizeAndStripToNull(".."));
        assertEquals(". .", StringUtils.normalizeAndStripToNull(" .  . "));
        assertEquals(". .", StringUtils.normalizeAndStripToNull("\t.\t\t.\t"));
        assertEquals(". .", StringUtils.normalizeAndStripToNull("\n.\n\n.\n"));
        assertEquals(". .", StringUtils.normalizeAndStripToNull("\r.\r\r.\r"));
        assertEquals(". .", StringUtils.normalizeAndStripToNull("\u00A0.\u00A0\u00A0.\u00A0"));
    }
}
