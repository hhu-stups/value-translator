package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BBooleanTest {
    @Test
    public void testTrue() throws TranslationException {
        final BBoolean v = Translator.translate("TRUE");
        assertThat(v.booleanValue()).isTrue();
    }

    @Test
    public void testFalse() throws TranslationException {
        final BBoolean v = Translator.translate("FALSE");
        assertThat(v.booleanValue()).isFalse();
    }
}
