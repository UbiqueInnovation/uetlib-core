package ch.ubique.backend.core.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void testWithMessage() throws ResourceNotFoundException {
        assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw ResourceNotFoundException.withMessage("with message");
                });
    }
}
