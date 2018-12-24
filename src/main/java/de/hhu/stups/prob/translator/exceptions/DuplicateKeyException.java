package de.hhu.stups.prob.translator.exceptions;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(final String message,
                                 final IllegalStateException exception) {
        super(message, exception);
    }

    public DuplicateKeyException(final String message) {
        super(message);
    }
}
