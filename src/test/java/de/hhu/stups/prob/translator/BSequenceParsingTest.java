package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BSequenceParsingTest{
    @Test
    public void sequenceToSet() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> value = Translator.translate("[a, b, c]");
        final Map<Integer, String> map = value.stream().collect(Collectors.toMap(
                i -> i.first().intValue(),
                j -> j.second().stringValue()));
        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }
    @Test
    public void newBSequence() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> value = Translator.translate("[a, b, c]");
        final Map<Integer, String> map = value.stream().collect(Collectors.toMap(
                i -> i.first().intValue(),
                j -> j.second().stringValue()));
        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }

    @Test
    public void emptySequence() throws BCompoundException {
        final BSet<BValue> value = Translator.translate("[]");
        assertTrue(value.toSet().isEmpty());
    }

    @Test
    public void sequenceAsSet() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> v1 = Translator.translate("[a, b, c]");
        final BSet<BTuple<BNumber, BAtom>> v2 = Translator.translate("{(1, a), (2, b), (3, c)}");
        assertEquals(v2, v1);
    }

}
