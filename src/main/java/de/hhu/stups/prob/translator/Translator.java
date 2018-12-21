package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.Node;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

public final class Translator {
    private Translator() {
        // only static access
    }

    public static <T extends BValue> T translate(final String expression)
            throws TranslationException {

        final Node ast;
        try {
            ast = BParser.parse("#EXPRESSION" + expression);
        } catch (final BCompoundException
                               | TranslatingVisitor.UnexpectedTypeException
                               | TranslatingVisitor.IllegalStateException
                         exception) {
            throw new TranslationException(exception);
        }
        final TranslatingVisitor<T> v = new TranslatingVisitor<>();
        ast.apply(v);

        return v.getResult();
    }
}
