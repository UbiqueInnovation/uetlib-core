package ch.uetlib.core.utils;

import static ch.uetlib.core.utils.NumberUtils.*;
import static ch.uetlib.test.assertion.AssertionUtils.assertThrowsWithMessage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

@SuppressWarnings({"DataFlowIssue", "ResultOfMethodCallIgnored", "ConstantValue"})
class NumberUtilsTest {

    // Tests for requirePositive methods

    @Test
    void testRequirePositiveShort_ValidValue_ShouldReturnSameValue() {
        final short input = 5;
        final var result = requirePositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequirePositiveShort_InvalidValue_ShouldThrowException() {
        final short input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requirePositive(input),
                "value must be positive");
    }

    @Test
    void testRequirePositiveInt_ValidValue_ShouldReturnSameValue() {
        final var input = 5;
        final var result = requirePositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequirePositiveInt_InvalidValue_ShouldThrowException() {
        final var input = 0;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requirePositive(input),
                "value must be positive");
    }

    @Test
    void testRequirePositiveLong_ValidValue_ShouldReturnSameValue() {
        final var input = 5L;
        final var result = requirePositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequirePositiveLong_InvalidValue_ShouldThrowException() {
        final var input = -1L;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requirePositive(input),
                "value must be positive");
    }

    @Test
    void testRequirePositiveDouble_ValidValue_ShouldReturnSameValue() {
        final var input = 5.0;
        final var result = requirePositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequirePositiveDouble_InvalidValue_ShouldThrowException() {
        final var input = -0.5;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requirePositive(input),
                "value must be positive");
    }

    @Test
    void testRequirePositiveFloat_ValidValue_ShouldReturnSameValue() {
        final var input = 5.0f;
        final var result = requirePositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequirePositiveFloat_InvalidValue_ShouldThrowException() {
        final var input = 0.0f;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requirePositive(input),
                "value must be positive");
    }

    @Test
    void testRequirePositiveNumber_ValidValue_ShouldReturnSameValue() {
        final Number input = 5;
        final var result = requirePositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequirePositiveNumber_InvalidValue_ShouldThrowException() {
        final Number input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requirePositive(input),
                "value must be positive");
    }

    @Test
    void testRequirePositiveNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                NullPointerException.class, () -> requirePositive(input), "value must be positive");
    }

    @Test
    void testRequirePositive_Overloads_ShouldBeCalled() {
        assertEquals((short) 1, requirePositive((short) 1, "ok"));
        assertEquals((short) 1, requirePositive((short) 1, () -> "ok"));
        assertEquals(1, requirePositive(1, "ok"));
        assertEquals(1, requirePositive(1, () -> "ok"));
        assertEquals(1L, requirePositive(1L, "ok"));
        assertEquals(1L, requirePositive(1L, () -> "ok"));
        assertEquals(1.0, requirePositive(1.0, "ok"));
        assertEquals(1.0, requirePositive(1.0, () -> "ok"));
        assertEquals(1.0f, requirePositive(1.0f, "ok"));
        assertEquals(1.0f, requirePositive(1.0f, () -> "ok"));
        assertEquals(1, requirePositive((Number) 1, "ok"));
        assertEquals(1, requirePositive((Number) 1, () -> "ok"));
    }

    // Tests for requireNonNegative methods

    @Test
    void testRequireNonNegativeShort_ValidValue_ShouldReturnSameValue() {
        final short input = 5;
        final var result = requireNonNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonNegativeShort_InvalidValue_ShouldThrowException() {
        final short input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input),
                "value must be non-negative");
    }

    @Test
    void testRequireNonNegativeInt_ValidValue_ShouldReturnSameValue() {
        final var input = 5;
        final var result = requireNonNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonNegativeInt_InvalidValue_ShouldThrowException() {
        final var input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input),
                "value must be non-negative");
    }

    @Test
    void testRequireNonNegativeLong_ValidValue_ShouldReturnSameValue() {
        final var input = 5L;
        final var result = requireNonNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonNegativeLong_InvalidValue_ShouldThrowException() {
        final var input = -1L;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input),
                "value must be non-negative");
    }

    @Test
    void testRequireNonNegativeDouble_ValidValue_ShouldReturnSameValue() {
        final var input = 5.0;
        final var result = requireNonNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonNegativeDouble_InvalidValue_ShouldThrowException() {
        final var input = -1.0;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input),
                "value must be non-negative");
    }

    @Test
    void testRequireNonNegativeFloat_ValidValue_ShouldReturnSameValue() {
        final var input = 5.0f;
        final var result = requireNonNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonNegativeFloat_InvalidValue_ShouldThrowException() {
        final var input = -0.5f;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input),
                "value must be non-negative");
    }

    @Test
    void testRequireNonNegativeNumber_ValidValue_ShouldReturnSameValue() {
        final Number input = 5;
        final var result = requireNonNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonNegativeNumber_InvalidValue_ShouldThrowException() {
        final Number input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input),
                "value must be non-negative");
    }

    @Test
    void testRequireNonNegativeNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> requireNonNegative(input),
                "value must be non-negative");
    }

    @Test
    void testRequireNonNegativeInt_CustomMessage_ShouldThrowException() {
        final var input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input, "Non-negative required"),
                "Non-negative required");
    }

    @Test
    void testRequireNonNegativeInt_MessageSupplier_ShouldThrowException() {
        final var input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input, () -> "Non-negative required"),
                "Non-negative required");
    }

    @Test
    void testRequireNonNegativeNumber_CustomMessage_ShouldThrowException() {
        final Number input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input, "Non-negative required"),
                "Non-negative required");
    }

    @Test
    void testRequireNonNegativeNumber_MessageSupplier_ShouldThrowException() {
        final Number input = -1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input, () -> "Non-negative required"),
                "Non-negative required");
    }

    @Test
    void testRequireNonNegative_Overloads_ShouldBeCalled() {
        assertEquals((short) 0, requireNonNegative((short) 0, "ok"));
        assertEquals((short) 0, requireNonNegative((short) 0, () -> "ok"));
        assertEquals(0, requireNonNegative(0, "ok"));
        assertEquals(0, requireNonNegative(0, () -> "ok"));
        assertEquals(0L, requireNonNegative(0L, "ok"));
        assertEquals(0L, requireNonNegative(0L, () -> "ok"));
        assertEquals(0.0, requireNonNegative(0.0, "ok"));
        assertEquals(0.0, requireNonNegative(0.0, () -> "ok"));
        assertEquals(0.0f, requireNonNegative(0.0f, "ok"));
        assertEquals(0.0f, requireNonNegative(0.0f, () -> "ok"));
        assertEquals(0, requireNonNegative((Number) 0, "ok"));
        assertEquals(0, requireNonNegative((Number) 0, () -> "ok"));
    }

    // Tests for requireNegative methods

    @Test
    void testRequireNegativeShort_ValidValue_ShouldReturnSameValue() {
        final short input = -5;
        final var result = requireNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNegativeShort_InvalidValue_ShouldThrowException() {
        final short input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input),
                "value must be negative");
    }

    @Test
    void testRequireNegativeInt_ValidValue_ShouldReturnSameValue() {
        final var input = -5;
        final var result = requireNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNegativeInt_InvalidValue_ShouldThrowException() {
        final var input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input),
                "value must be negative");
    }

    @Test
    void testRequireNegativeLong_ValidValue_ShouldReturnSameValue() {
        final var input = -5L;
        final var result = requireNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNegativeLong_InvalidValue_ShouldThrowException() {
        final var input = 1L;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input),
                "value must be negative");
    }

    @Test
    void testRequireNegativeDouble_ValidValue_ShouldReturnSameValue() {
        final var input = -5.0;
        final var result = requireNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNegativeDouble_InvalidValue_ShouldThrowException() {
        final var input = 5.0;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input),
                "value must be negative");
    }

    @Test
    void testRequireNegativeFloat_ValidValue_ShouldReturnSameValue() {
        final var input = -5.0f;
        final var result = requireNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNegativeFloat_InvalidValue_ShouldThrowException() {
        final var input = 5.0f;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input),
                "value must be negative");
    }

    @Test
    void testRequireNegativeNumber_ValidValue_ShouldReturnSameValue() {
        final Number input = -5;
        final var result = requireNegative(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNegativeNumber_InvalidValue_ShouldThrowException() {
        final Number input = 5;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input),
                "value must be negative");
    }

    @Test
    void testRequireNegativeNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                NullPointerException.class, () -> requireNegative(input), "value must be negative");
    }

    @Test
    void testRequireNegativeInt_CustomMessage_ShouldThrowException() {
        final var input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input, "Negative required"),
                "Negative required");
    }

    @Test
    void testRequireNegativeInt_MessageSupplier_ShouldThrowException() {
        final var input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input, () -> "Negative required"),
                "Negative required");
    }

    @Test
    void testRequireNegativeNumber_CustomMessage_ShouldThrowException() {
        final Number input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input, "Negative required"),
                "Negative required");
    }

    @Test
    void testRequireNegativeNumber_MessageSupplier_ShouldThrowException() {
        final Number input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input, () -> "Negative required"),
                "Negative required");
    }

    @SuppressWarnings("UnnecessaryBoxing")
    @Test
    void testRequireNegative_Overloads_ShouldBeCalled() {
        assertEquals((short) -1, requireNegative((short) -1, "ok"));
        assertEquals((short) -1, requireNegative((short) -1, () -> "ok"));
        assertEquals(-1, requireNegative(-1, "ok"));
        assertEquals(-1, requireNegative(-1, () -> "ok"));
        assertEquals(-1L, requireNegative(-1L, "ok"));
        assertEquals(-1L, requireNegative(-1L, () -> "ok"));
        assertEquals(-1.0, requireNegative(-1.0, "ok"));
        assertEquals(-1.0, requireNegative(-1.0, () -> "ok"));
        assertEquals(-1.0f, requireNegative(-1.0f, "ok"));
        assertEquals(-1.0f, requireNegative(-1.0f, () -> "ok"));
        assertEquals(-1, requireNegative((Number) Integer.valueOf(-1), "ok"));
        assertEquals(-1, requireNegative((Number) Integer.valueOf(-1), () -> "ok"));
    }

    // Tests for requireNonPositive methods

    @Test
    void testRequireNonPositiveShort_ValidValue_ShouldReturnSameValue() {
        final short input = 0;
        final var result = requireNonPositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonPositiveShort_InvalidValue_ShouldThrowException() {
        final short input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input),
                "value must be non-positive");
    }

    @Test
    void testRequireNonPositiveInt_ValidValue_ShouldReturnSameValue() {
        final var input = 0;
        final var result = requireNonPositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonPositiveInt_InvalidValue_ShouldThrowException() {
        final var input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input),
                "value must be non-positive");
    }

    @Test
    void testRequireNonPositiveLong_ValidValue_ShouldReturnSameValue() {
        final var input = 0L;
        final var result = requireNonPositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonPositiveLong_InvalidValue_ShouldThrowException() {
        final var input = 1L;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input),
                "value must be non-positive");
    }

    @Test
    void testRequireNonPositiveDouble_ValidValue_ShouldReturnSameValue() {
        final var input = 0.0;
        final var result = requireNonPositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonPositiveDouble_InvalidValue_ShouldThrowException() {
        final var input = 1.0;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input),
                "value must be non-positive");
    }

    @Test
    void testRequireNonPositiveFloat_ValidValue_ShouldReturnSameValue() {
        final var input = 0.0f;
        final var result = requireNonPositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonPositiveFloat_InvalidValue_ShouldThrowException() {
        final var input = 1.0f;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input),
                "value must be non-positive");
    }

    @Test
    void testRequireNonPositiveNumber_ValidValue_ShouldReturnSameValue() {
        final Number input = 0;
        final var result = requireNonPositive(input);
        assertEquals(input, result);
    }

    @Test
    void testRequireNonPositiveNumber_InvalidValue_ShouldThrowException() {
        final Number input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input),
                "value must be non-positive");
    }

    @Test
    void testRequireNonPositiveNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                NullPointerException.class,
                () -> requireNonPositive(input),
                "value must be non-positive");
    }

    @Test
    void testRequireNonPositiveInt_CustomMessage_ShouldThrowException() {
        final var input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input, "Non-positive required"),
                "Non-positive required");
    }

    @Test
    void testRequireNonPositiveInt_MessageSupplier_ShouldThrowException() {
        final var input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input, () -> "Non-positive required"),
                "Non-positive required");
    }

    @Test
    void testRequireNonPositiveNumber_CustomMessage_ShouldThrowException() {
        final Number input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input, "Non-positive required"),
                "Non-positive required");
    }

    @Test
    void testRequireNonPositiveNumber_MessageSupplier_ShouldThrowException() {
        final Number input = 1;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input, () -> "Non-positive required"),
                "Non-positive required");
    }

    @Test
    void testRequireNonPositive_Overloads_ShouldBeCalled() {
        assertEquals((short) 0, requireNonPositive((short) 0, "ok"));
        assertEquals((short) 0, requireNonPositive((short) 0, () -> "ok"));
        assertEquals(0, requireNonPositive(0, "ok"));
        assertEquals(0, requireNonPositive(0, () -> "ok"));
        assertEquals(0L, requireNonPositive(0L, "ok"));
        assertEquals(0L, requireNonPositive(0L, () -> "ok"));
        assertEquals(0.0, requireNonPositive(0.0, "ok"));
        assertEquals(0.0, requireNonPositive(0.0, () -> "ok"));
        assertEquals(0.0f, requireNonPositive(0.0f, "ok"));
        assertEquals(0.0f, requireNonPositive(0.0f, () -> "ok"));
        assertEquals(0, requireNonPositive((Number) 0, "ok"));
        assertEquals(0, requireNonPositive((Number) 0, () -> "ok"));
    }
}
