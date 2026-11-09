package ch.uetlib.core.reflection;

import static org.junit.jupiter.api.Assertions.*;

import ch.uetlib.core.reflection.ReflectionUtilsTest.TestRecord;

import org.junit.jupiter.api.Test;

class IntrospectionWrapperTest {

    @Test
    void getPropertyValue() {
        final var object = new TestObject();
        final var expectedValue = 1;

        final var introspectionWrapper = new IntrospectionWrapper(object);
        final var actualValue = introspectionWrapper.getPropertyValue("field");

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void getPropertyValueFromPrivateField() {
        final var object = new TestObject();
        final var expectedValue = 2;
        final var introspectionWrapper = new IntrospectionWrapper(object);

        final var actualValue = introspectionWrapper.getPropertyValue("privateField");

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void getPropertyValueFromRecord() {
        final var object = new TestRecord("someString");
        final var expectedValue = "someString";
        final var introspectionWrapper = new IntrospectionWrapper(object);

        final var actualValue = introspectionWrapper.getPropertyValue("field");

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void getPropertyValueNoSuchField() {
        final var object = new TestObject();
        final var fieldName = "nonExistentField";
        final var introspectionWrapper = new IntrospectionWrapper(object);
        assertThrows(
                IntrospectionException.class,
                () -> introspectionWrapper.getPropertyValue(fieldName));
    }

    @Test
    void getPropertyValueWhichIsNull() {
        final var object = new TestRecord(null);
        final var fieldName = "field";
        final var introspectionWrapper = new IntrospectionWrapper(object);

        final var actualValue = introspectionWrapper.getPropertyValue(fieldName);

        assertNull(actualValue);
    }

    @SuppressWarnings({"java:S1068", "unused"})
    private static class TestObject {
        private final int privateField = 2;
        public int field = 1;
    }
}
