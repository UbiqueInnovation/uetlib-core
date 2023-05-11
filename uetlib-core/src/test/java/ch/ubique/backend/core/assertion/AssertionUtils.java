package ch.ubique.backend.core.assertion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public final class AssertionUtils {
    private AssertionUtils() {}

    public static <T extends Throwable> void assertThrowsWithMessage(
            final Class<T> expectedType,
            final Executable executable,
            final String expectedMessage) {
        final var exception = Assertions.assertThrows(expectedType, executable);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    public static <T extends Throwable, U extends Throwable> void assertThrowsWithMessageAndCause(
            final Class<T> expectedType,
            final Executable executable,
            final String expectedMessage,
            final Class<U> causeType) {
        final var exception = Assertions.assertThrows(expectedType, executable);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
        // runtime version of `instanceof causeType` check
        Assertions.assertTrue(causeType.isInstance(exception.getCause()));
    }
}
