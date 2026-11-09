package ch.uetlib.core.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    @SuppressWarnings("java:S5778")
    void testConstructor() throws ResourceNotFoundException {
        assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw new ResourceNotFoundException();
                });
        assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw new ResourceNotFoundException("with message");
                });
        assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw new ResourceNotFoundException(
                            "with message", new IllegalStateException());
                });
    }

    @Test
    void testWithMessage() throws ResourceNotFoundException {
        assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw ResourceNotFoundException.withMessage("with message");
                });
    }

    @Test
    @SuppressWarnings("java:S5778")
    void testThrowsWithMessage() throws ResourceNotFoundException {
        assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw ResourceNotFoundException.supplierWithMessage("with message").get();
                });
    }
}
