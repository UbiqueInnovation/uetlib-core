package ch.ubique.backend.core.exception;

import jakarta.validation.constraints.NotNull;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3763002992193794029L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException withMessage(
            @NotNull final String pattern, final Object... arguments) {
        return new ResourceNotFoundException(MessageFormat.format(pattern, arguments));
    }

    public static Supplier<ResourceNotFoundException> throwWithMessage(
            @NotNull final String pattern, final Object... arguments) {
        return () -> new ResourceNotFoundException(MessageFormat.format(pattern, arguments));
    }
}
