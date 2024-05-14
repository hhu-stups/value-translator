package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.AExpressionParseUnit;
import de.be4.classicalb.core.parser.node.PExpression;
import de.be4.classicalb.core.parser.node.PParseUnit;
import de.be4.classicalb.core.parser.node.Start;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

public final class Translator {
    private Translator() {
        // only static access
    }

    public static <T extends BValue> T translate(final PExpression ast)
            throws TranslationException {
        try {
            final TranslatingVisitor<T> visitor = new TranslatingVisitor<>();
            ast.apply(visitor);
            return visitor.getResult();
        } catch (final TranslatingVisitor.IllegalStateException exception) {
            throw new TranslationException(exception);
        }
    }

    public static <T extends BValue> T translate(final Start ast)
            throws TranslationException {
        final PParseUnit parseUnit = ast.getPParseUnit();
        if (!(parseUnit instanceof AExpressionParseUnit)) {
            throw new TranslationException(
                new TranslatingVisitor.IllegalStateException(
                    "Only expressions can be translated, but received a "
                    + parseUnit.getClass()
                )
            );
        }
        return translate(((AExpressionParseUnit) parseUnit).getExpression());
    }

    public static <T extends BValue> T translate(final String expression)
            throws TranslationException {
        final Start ast;
        try {
            ast = BParser.parse("#EXPRESSION" + expression);
        } catch (final BCompoundException exception) {
            throw new TranslationException(exception);
        }
        return translate(ast);
    }
}
