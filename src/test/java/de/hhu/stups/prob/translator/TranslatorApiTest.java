package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.AIntegerExpression;
import de.be4.classicalb.core.parser.node.Start;
import de.be4.classicalb.core.parser.node.TIntegerLiteral;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Please only put general tests here that don't fit anywhere else!
 * Any tests that relate to a specific value type should go into that type's
 * test class instead.
 */
final class TranslatorApiTest {
    @Test
    void translateAstStart() throws BCompoundException, TranslationException {
        final Start ast = new BParser().parseExpression("1");
        final BNumber result = Translator.translate(ast);
        assertThat(result.intValue()).isEqualTo(1);
    }

    @Test
    void translateAstExpression() throws TranslationException {
        final AIntegerExpression ast = new AIntegerExpression(
            new TIntegerLiteral("1")
        );
        final BNumber result = Translator.translate(ast);
        assertThat(result.intValue()).isEqualTo(1);
    }
}
