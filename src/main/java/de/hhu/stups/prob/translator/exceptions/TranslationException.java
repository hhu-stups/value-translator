package de.hhu.stups.prob.translator.exceptions;

public class TranslationException extends Exception {
    private static final long serialVersionUID = 965400601579654832L;

    public TranslationException(final Exception exception) {
        super(exception);
    }
}
