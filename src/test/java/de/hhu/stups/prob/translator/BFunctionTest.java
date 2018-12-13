package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BFunctionTest{

    @Test
    public void functionToMap() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (2, b), (3,c)}");
        final Map<BNumber, BAtom> map = set.asFunction(BNumber.class, BAtom.class).toMap();
        assertEquals(new BAtom("a"), map.get(new BNumber(1)));
        assertEquals(new BAtom("b"), map.get(new BNumber(2)));
        assertEquals(new BAtom("c"), map.get(new BNumber(3)));

    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void functionToMapDuplicateKeys() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BFunction<BNumber, BAtom> function = set.asFunction();
        function.toMap();
    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void functionToMapWithExtractorDuplicateKeys() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BFunction<BNumber, BAtom> function = set.asFunction();
        function.toMap(BNumber::intValue, BAtom::stringValue);
    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void newFunction() throws BCompoundException {
        final BSet<BNumber> set = Translator.translate("{1,2,3}");
        set.asFunction();
    }

    @Test
    public void newFunction2() throws BCompoundException {
        final BSet<BValue> set = Translator.translate("{(1 |-> 2 |-> 3), (2 |-> 3 |-> 4)}");
        final BFunction<BTuple<BNumber, BNumber>, BNumber> function = set.asFunction();
        final Map<Long, Long> map = function.toMap(
                tuple -> tuple.first().longValue() + tuple.second().longValue(),
                BNumber::longValue);
        assertEquals(Long.valueOf(3), map.get(3L));
        assertEquals(Long.valueOf(4), map.get(5L));

    }

    @Test
    public void functionToMapExtractValues() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (2, b), (3,c)}");

        final Map<Integer, String> map
                = set.asFunction(BNumber.class, BAtom.class)
                          .toMap(BNumber::intValue, BAtom::stringValue);

        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }

}
