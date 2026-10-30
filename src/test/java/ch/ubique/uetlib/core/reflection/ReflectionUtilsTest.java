package ch.ubique.uetlib.core.reflection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReflectionUtilsTest {

    @Test
    void getFieldValue() {
        final var object = new TestObject();
        final var fieldName = "field";
        final var expectedValue = 1;

        try {
            final var actualValue = ReflectionUtils.getFieldValue(object, fieldName);
            assertEquals(expectedValue, actualValue);
        } catch (final Exception e) {
            fail(e);
        }
    }

    @Test
    void getFieldValueFromPrivateField() {
        final var object = new TestObject();
        final var fieldName = "privateField";
        final var expectedValue = 2;

        try {
            final var actualValue = ReflectionUtils.getFieldValue(object, fieldName);
            assertEquals(expectedValue, actualValue);
        } catch (final Exception e) {
            fail(e);
        }
    }

    @Test
    void getFieldValueFromRecord() {
        final var object = new TestRecord("someString");
        final var fieldName = "field";
        final var expectedValue = "someString";

        try {
            final var actualValue = ReflectionUtils.getFieldValue(object, fieldName);
            assertEquals(expectedValue, actualValue);
        } catch (final Exception e) {
            fail(e);
        }
    }

    @Test
    void getFieldValueNoSuchField() {
        final var object = new TestObject();
        final var fieldName = "nonExistentField";

        assertThrows(
                NoSuchFieldException.class, () -> ReflectionUtils.getFieldValue(object, fieldName));
    }

    @Test
    void getFieldValueWhichIsNull() {
        final var object = new TestRecord(null);
        final var fieldName = "field";

        try {
            final var actualValue = ReflectionUtils.getFieldValue(object, fieldName);
            assertNull(actualValue);
        } catch (final Exception e) {
            fail(e);
        }
    }

    @SuppressWarnings({"java:S1068", "unused"})
    private static class TestObject {
        private final int privateField = 2;
        public int field = 1;
    }

    record TestRecord(String field) {}
}
