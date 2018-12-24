package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BBooleanTest {
    @Test
    public void testTrue() throws TranslationException {
        final BBoolean v = Translator.translate("TRUE");
        assertTrue(v.booleanValue());
    }

    @Test
    public void testFalse() throws TranslationException {
        final BBoolean v = Translator.translate("FALSE");
        assertFalse(v.booleanValue());
    }
}
