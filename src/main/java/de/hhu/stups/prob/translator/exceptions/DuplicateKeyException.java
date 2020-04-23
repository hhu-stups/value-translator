package de.hhu.stups.prob.translator.exceptions;

public class DuplicateKeyException extends RuntimeException {
    private static final long serialVersionUID = -5874860354755056316L;

    public DuplicateKeyException(final String message,
                                 final IllegalStateException exception) {
        super(message, exception);
    }

    public DuplicateKeyException(final String message) {
        super(message);
    }
}
