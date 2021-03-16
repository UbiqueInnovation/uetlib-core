package ch.ubique.backend.core.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Whenever a ResourceNotFoundException is thrown in a spring controller method that is not handled
 * elsewhere, spring will return a 404 as the defined by the @ResponseStatus annotation here.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) // 404
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
