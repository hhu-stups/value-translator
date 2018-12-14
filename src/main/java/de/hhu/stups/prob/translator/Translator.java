package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.Node;

public final class Translator{
    private Translator() {
        // only static access
    }

    // TODO: custom exception
    public static <T extends BValue> T translate(final String expression) throws BCompoundException {
        final Node ast = BParser.parse("#EXPRESSION" + expression);
        final TranslatingVisitor<T> v = new TranslatingVisitor<>();
        ast.apply(v);

        return v.getResult();
    }
}
