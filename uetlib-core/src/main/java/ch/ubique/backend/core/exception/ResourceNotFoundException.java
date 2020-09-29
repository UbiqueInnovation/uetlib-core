package ch.ubique.backend.core.exception;

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
}
