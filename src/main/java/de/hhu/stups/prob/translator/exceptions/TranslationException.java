package de.hhu.stups.prob.translator.exceptions;

public class TranslationException extends Exception{
    public TranslationException(final Exception exception) {
        super(exception);
    }
}
