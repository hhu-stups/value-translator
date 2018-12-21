package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class BAtomTest {

    private static final String ATOM = "atom";
    private BAtom atom;

    @Before
    public void setUp() throws TranslationException {
        this.atom = Translator.translate(ATOM);
    }

    @Test
    public void testToString() {
        assertEquals(ATOM, this.atom.toString());
    }

    @Test
    public void testEquals() throws TranslationException {
        assertEquals(Translator.translate(ATOM), this.atom);
        assertNotEquals(Translator.translate("other"), this.atom);
    }

    @Test
    public void testStringValue() {
        assertEquals(ATOM, this.atom.stringValue());
    }
}
