package ch.ubique.backend.core.utils;

import static ch.ubique.backend.core.utils.NumberUtils.*;
import static ch.ubique.backend.test.assertion.AssertionUtils.assertThrowsWithMessage;

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
                "Value must be positive");
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
                "Value must be positive");
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
                "Value must be positive");
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
                "Value must be positive");
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
                "Value must be positive");
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
                "Value must be positive");
    }

    @Test
    void testRequirePositiveNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requirePositive(input),
                "Value must be positive");
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
                "Value must be non-negative");
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
                "Value must be non-negative");
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
                "Value must be non-negative");
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
                "Value must be non-negative");
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
                "Value must be non-negative");
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
                "Value must be non-negative");
    }

    @Test
    void testRequireNonNegativeNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonNegative(input),
                "Value must be non-negative");
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
                "Value must be negative");
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
                "Value must be negative");
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
                "Value must be negative");
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
                "Value must be negative");
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
                "Value must be negative");
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
                "Value must be negative");
    }

    @Test
    void testRequireNegativeNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNegative(input),
                "Value must be negative");
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
                "Value must be non-positive");
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
                "Value must be non-positive");
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
                "Value must be non-positive");
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
                "Value must be non-positive");
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
                "Value must be non-positive");
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
                "Value must be non-positive");
    }

    @Test
    void testRequireNonPositiveNumber_NullValue_ShouldThrowException() {
        final Number input = null;
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> requireNonPositive(input),
                "Value must be non-positive");
    }
}
