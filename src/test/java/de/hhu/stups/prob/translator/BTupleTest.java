package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressFBWarnings(value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
public class BTupleTest {
    @Test
    public void testComponents() throws TranslationException {
        final BTuple<BNumber, BNumber> t1
                = Translator.translate("(1 |-> 2)");
        assertEquals(new BNumber(1), t1.getFirst());
        assertEquals(new BNumber(2), t1.getSecond());
    }

    @Test
    public void testComponents2() throws TranslationException {
        final BTuple<BNumber, BNumber> t1
                = Translator.translate("(1, 2)");
        assertEquals(new BNumber(1), t1.getFirst());
        assertEquals(new BNumber(2), t1.getSecond());
    }

    @Test
    public void testNested() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> result
                = Translator.translate("(a |-> 1 |-> b)");
        assertEquals("a", result.getFirst().getFirst().stringValue());
        assertEquals(1, result.getFirst().getSecond().intValue());
        assertEquals("b", result.getSecond().stringValue());
    }

    @Test
    public void testManualNested() throws TranslationException {
        final BTuple<BAtom, BTuple<BNumber, BAtom>> result
                = Translator.translate("(a |-> (1 |-> b))");
        assertEquals("a", result.getFirst().stringValue());
        assertEquals(1, result.getSecond().getFirst().intValue());
        assertEquals("b", result.getSecond().getSecond().stringValue());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testNestedTyping() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> result2
                = Translator.translate("(99 |-> 1 |-> b)");
        final BNumber actual
                = ((BNumber) ((Object) result2.getFirst().getFirst()));
        final int expected = 99;
        assertEquals(expected, actual.intValue());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testNestedTyping2() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> tuple
                = Translator.translate("(99 |-> 1 |-> b)");
        assertThrows(ClassCastException.class,
                () -> tuple.getFirst().getFirst().stringValue());
    }
}
