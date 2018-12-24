package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings({
        "PMD.BeanMembersShouldSerialize",
        "JUnitAssertionsShouldIncludeMessage",
        "AtLeastOneConstructor"})
public class BAtomTest {

    private static final String ATOM = "atom";
    private static BAtom atom;

    @BeforeAll
    public static void setUp() throws TranslationException {
        BAtomTest.atom = Translator.translate(ATOM);
    }

    @Test
    public void testToString() {
        assertEquals(ATOM, String.valueOf(atom));
    }

    @Test
    public void testEquals() {
        assertEquals(new BAtom(ATOM), atom);
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(new BAtom("other"), atom);
    }

    @Test
    public void testStringValue() {
        assertEquals(ATOM, atom.stringValue());
    }
}
