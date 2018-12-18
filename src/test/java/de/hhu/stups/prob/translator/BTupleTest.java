package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BTupleTest{
    @Test
    public void testComponents() throws TranslationException {
        final BTuple<BNumber, BNumber> t1 = Translator.translate("(1 |-> 2)");
        assertEquals(new BNumber(1), t1.first());
        assertEquals(new BNumber(2), t1.second());
    }

    @Test
    public void testNested() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> result = Translator.translate("(a |-> 1 |-> b)");
        assertEquals("a", result.first().first().stringValue());
        assertEquals(1, result.first().second().intValue());
        assertEquals("b", result.second().stringValue());
    }

    @Test
    public void testManualNested() throws TranslationException {
        final BTuple<BAtom, BTuple<BNumber, BAtom>> result = Translator.translate("(a |-> (1 |-> b))");
        assertEquals("a", result.first().stringValue());
        assertEquals(1, result.second().first().intValue());
        assertEquals("b", result.second().second().stringValue());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testNestedTyping() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> result2 = Translator.translate("(99 |-> 1 |-> b)");
        assertEquals(99, ((BNumber) ((Object) result2.first().first())).intValue());
    }

    @Test(expected = ClassCastException.class)
    @SuppressFBWarnings(value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
            justification = "Call used to trigger an exception.")
    public void testNestedTyping2() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> tuple = Translator.translate("(99 |-> 1 |-> b)");
        tuple.first().first().stringValue();
    }
}
