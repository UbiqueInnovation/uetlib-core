package ch.ubique.backend.core.exception;

import jakarta.validation.constraints.NotNull;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3763002992193794029L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException withMessage(
            @NotNull String pattern, Object... arguments) {
        return new ResourceNotFoundException(MessageFormat.format(pattern, arguments));
    }

    public static Supplier<ResourceNotFoundException> throwWithMessage(
            @NotNull String pattern, Object... arguments) {
        return () -> new ResourceNotFoundException(MessageFormat.format(pattern, arguments));
    }
}
