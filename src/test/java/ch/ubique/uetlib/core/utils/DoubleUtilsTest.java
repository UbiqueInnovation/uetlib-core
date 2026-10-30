package ch.ubique.uetlib.core.utils;

import static ch.ubique.uetlib.core.utils.DoubleUtils.nullIfNaN;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

@SuppressWarnings("ConstantValue")
class DoubleUtilsTest {

    @Test
    void testNullIfNaN_WithNaNValue_ShouldReturnNull() {
        final Double input = Double.NaN;
        final var result = nullIfNaN(input);
        assertNull(result, "The result should be null when the input is NaN.");
    }

    @Test
    void testNullIfNaN_WithNonNaNValue_ShouldReturnSameValue() {
        final Double input = 1.23;
        final var result = nullIfNaN(input);
        assertEquals(
                input, result, "The result should be the same value when the input is not NaN.");
    }

    @Test
    void testNullIfNaN_WithNullValue_ShouldReturnNull() {
        final Double input = null;
        final var result = nullIfNaN(input);
        assertNull(result, "The result should be null when the input is null.");
    }

    @Test
    void testNullIfNaN_WithNegativeInfinity_ShouldReturnNegativeInfinity() {
        final Double input = Double.NEGATIVE_INFINITY;
        final var result = nullIfNaN(input);
        assertEquals(
                input,
                result,
                "The result should be the same value when the input is negative infinity.");
    }

    @Test
    void testNullIfNaN_WithPositiveInfinity_ShouldReturnPositiveInfinity() {
        final Double input = Double.POSITIVE_INFINITY;
        final var result = nullIfNaN(input);
        assertEquals(
                input,
                result,
                "The result should be the same value when the input is positive infinity.");
    }
}
