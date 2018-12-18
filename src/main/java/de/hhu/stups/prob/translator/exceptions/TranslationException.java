package de.hhu.stups.prob.translator.exceptions;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;

public class TranslationException extends Exception{
    public TranslationException(final BCompoundException exception) {
        super(exception);
    }
}
