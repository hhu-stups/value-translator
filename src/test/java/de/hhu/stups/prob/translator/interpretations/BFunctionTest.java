package de.hhu.stups.prob.translator.interpretations;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.TranslationException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BFunctionTest{

    @Test
    public void functionToMap() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (2, b), (3,c)}");
        final Map<BNumber, BAtom> map = set.asFunction(BNumber.class, BAtom.class).toMap();
        assertEquals(new BAtom("a"), map.get(new BNumber(1)));
        assertEquals(new BAtom("b"), map.get(new BNumber(2)));
        assertEquals(new BAtom("c"), map.get(new BNumber(3)));

    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void functionToMapDuplicateKeys() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BFunction<BNumber, BAtom> function = set.asFunction();
        function.toMap();
    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void functionToMapWithExtractorDuplicateKeys() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BFunction<BNumber, BAtom> function = set.asFunction();
        function.toMap(BNumber::intValue, BAtom::stringValue);
    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void newFunction() throws TranslationException {
        final BSet<BNumber> set = Translator.translate("{1,2,3}");
        set.asFunction();
    }

    @Test
    public void newFunction2() throws TranslationException {
        final BSet<BValue> set = Translator.translate("{(1 |-> 2 |-> 3), (2 |-> 3 |-> 4)}");
        final BFunction<BTuple<BNumber, BNumber>, BNumber> function = set.asFunction();
        final Map<Long, Long> map = function.toMap(
                tuple -> tuple.first().longValue() + tuple.second().longValue(),
                BNumber::longValue);
        assertEquals(Long.valueOf(3), map.get(3L));
        assertEquals(Long.valueOf(4), map.get(5L));

    }

    @Test
    public void functionToMapExtractValues() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (2, b), (3,c)}");

        final Map<Integer, String> map
                = set.asFunction(BNumber.class, BAtom.class)
                          .toMap(BNumber::intValue, BAtom::stringValue);

        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }

    @SuppressWarnings("unused")
    @SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE",
            justification = "Type of the variable is needed to trigger a ClassCastException.")
    @Test(expected = ClassCastException.class)
    public void translateToFunction() throws TranslationException {
        final BFunction<BNumber, BAtom> set = Translator.translate("{(1,a), (2, b), (3,c)}");
    }

    @SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE",
            justification = "Type of the variable is needed to trigger a ClassCastException.")
    @SuppressWarnings("unused")
    @Test(expected = ClassCastException.class)
    public void translateToSetOfFunctions() throws TranslationException {
        final BSet<BFunction<BNumber, BAtom>> set = Translator.translate("{{(1,a), (2, b), (3,c)}}");
        final BFunction<BNumber, BAtom> func = set.toSet().iterator().next();
    }

}
