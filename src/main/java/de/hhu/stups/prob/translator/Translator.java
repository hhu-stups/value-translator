package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.Node;
import de.be4.classicalb.core.parser.node.Start;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

public final class Translator {
    private Translator() {
        // only static access
    }

    public static <T extends BValue> T translate(final Node node)
            throws TranslationException {
        try {
            final TranslatingVisitor<T> visitor = new TranslatingVisitor<>();
            node.apply(visitor);
            return visitor.getResult();
        } catch (final TranslatingVisitor.UncheckedException exception) {
            throw new TranslationException(exception);
        }
    }

    public static <T extends BValue> T translate(final String expression)
            throws TranslationException {
        final Start ast;
        try {
            ast = new BParser().parseFormula(expression);
        } catch (final BCompoundException exception) {
            throw new TranslationException(exception);
        }
        return translate(ast);
    }
}
