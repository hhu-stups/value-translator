package de.hhu.stups.prob.translator.exceptions;

public class DuplicateKeyException extends RuntimeException{
    public DuplicateKeyException(final String format) {
        super(format);
    }
}
