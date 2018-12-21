package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.Test;

import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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
        Set s;
    }
}
