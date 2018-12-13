package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BSequenceTest{

    @Test
    public void setAsSequence() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> value = Translator.translate("[c, b, a]");
        final BSequence<BAtom> sequence = value.asSequence();
        final Map<Integer, String> map = sequence.toMap(BNumber::intValue, BAtom::stringValue);
        assertEquals("c", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("a", map.get(3));
    }

    @SuppressWarnings("FeatureEnvy")
    @Test
    public void toList2() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> value = Translator.translate("{(1, a), (2, b), (3, c)}");
        final List<BAtom> list = value.asSequence(BAtom.class).toList();
        assertEquals("a", list.get(0).stringValue());
        assertEquals("b", list.get(1).stringValue());
        assertEquals("c", list.get(2).stringValue());
    }

    @Test
    public void toList() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> value = Translator.translate("[c, b, a]");
        final List<BAtom> list = value.asSequence(BAtom.class).toList();

        assertEquals("c", list.get(0).stringValue());
        assertEquals("b", list.get(1).stringValue());
        assertEquals("a", list.get(2).stringValue());
    }

    @Test(expected = RuntimeException.class) // TODO: improve error generated here
    public void toListError() throws BCompoundException {
        Translator.<BSet<?>>translate("{1,2,3}").asSequence().toList();
    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void sequenceToMapDuplicateKeys() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (2,c)}");
        final BSequence<BAtom> function = set.asSequence();
        function.toList();
    }

    @Test(expected = RuntimeException.class) // TODO: custom error class
    public void sequenceToMapWithExtractorDuplicateKeys() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BSequence<BAtom> function = set.asSequence();
        function.toList(BAtom::stringValue);
    }
}