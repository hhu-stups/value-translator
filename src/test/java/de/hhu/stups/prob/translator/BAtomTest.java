package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings({
        "PMD.BeanMembersShouldSerialize",
        "JUnitAssertionsShouldIncludeMessage",
        "AtLeastOneConstructor"})
public class BAtomTest {

    private static final String ATOM = "atom";
    private BAtom atom;

    @Before
    public void setUp() throws TranslationException {
        this.atom = Translator.translate(ATOM);
    }

    @Test
    public void testToString() {
        assertEquals(ATOM, String.valueOf(this.atom));
    }

    @Test
    public void testEquals() {
        assertEquals(new BAtom(ATOM), this.atom);
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(new BAtom("other"), this.atom);
    }

    @Test
    public void testStringValue() {
        assertEquals(ATOM, this.atom.stringValue());
    }
}
