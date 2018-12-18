package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BAtomTest{

    BAtom atom;

    @Before
    public void setUp() throws TranslationException {
        this.atom = Translator.translate("atom");
    }

    @Test
    public void testToString() {
        assertEquals("atom", this.atom.toString());
    }

    @Test
    public void testEquals() throws TranslationException {
        assertEquals(Translator.translate("atom"), this.atom);
        assertNotEquals(Translator.translate("other"), this.atom);
    }

    @Test
    public void testStringValue() {
        assertEquals("atom", this.atom.stringValue());
    }
}