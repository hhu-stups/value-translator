package de.hhu.stups.prob.translator.interpretations;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("checkstyle:magicnumber")
public class BSequenceParsingTest {
    @Test
    public void sequenceToSet() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> value
                = Translator.translate("[a, b, c]");
        final Map<Integer, String> map
                = value.stream().collect(Collectors.toMap(
                i -> i.getFirst().intValue(),
                j -> j.getSecond().stringValue()));
        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }

    @Test
    public void newBSequence() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> value
                = Translator.translate("[a, b, c]");
        final Map<Integer, String> map
                = value.stream().collect(Collectors.toMap(
                i -> i.getFirst().intValue(),
                j -> j.getSecond().stringValue()));
        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }

    @Test
    public void emptySequence() throws TranslationException {
        final BSet<BValue> value = Translator.translate("[]");
        assertTrue(value.toSet().isEmpty());
    }

    @Test
    public void sequenceAsSet() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> v1
                = Translator.translate("[a, b, c]");
        final BSet<BTuple<BNumber, BAtom>> v2
                = Translator.translate("{(1, a), (2, b), (3, c)}");
        assertEquals(v2, v1);
    }

}
