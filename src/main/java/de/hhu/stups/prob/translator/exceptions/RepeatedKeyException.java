package de.hhu.stups.prob.translator.exceptions;

public class RepeatedKeyException extends RuntimeException{
    public RepeatedKeyException(final String format) {
        super(format);
    }
}
