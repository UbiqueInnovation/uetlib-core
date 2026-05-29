package ch.ubique.backend.core.exception;

import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException {

    @Serial private static final long serialVersionUID = -3763002992193794029L;

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException withMessage(
            final String pattern, final Object... arguments) {
        return new ResourceNotFoundException(MessageFormat.format(pattern, arguments));
    }

    public static Supplier<ResourceNotFoundException> supplierWithMessage(
            final String pattern, final Object... arguments) {
        return () -> withMessage(pattern, arguments);
    }
}
