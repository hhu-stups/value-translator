package de.hhu.stups.prob.translator.interpretations;

import java.util.Map;
import java.util.stream.Collectors;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(map.get(1)).isEqualTo("a");
        assertThat(map.get(2)).isEqualTo("b");
        assertThat(map.get(3)).isEqualTo("c");
    }

    @Test
    public void newBSequence() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> value
                = Translator.translate("[a, b, c]");
        final Map<Integer, String> map
                = value.stream().collect(Collectors.toMap(
                i -> i.getFirst().intValue(),
                j -> j.getSecond().stringValue()));
        assertThat(map.get(1)).isEqualTo("a");
        assertThat(map.get(2)).isEqualTo("b");
        assertThat(map.get(3)).isEqualTo("c");
    }

    @Test
    public void emptySequence() throws TranslationException {
        final BSet<BValue> value = Translator.translate("[]");
        assertThat(value.toSet().isEmpty()).isTrue();
    }

    @Test
    public void sequenceAsSet() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> v1
                = Translator.translate("[a, b, c]");
        final BSet<BTuple<BNumber, BAtom>> v2
                = Translator.translate("{(1, a), (2, b), (3, c)}");
        assertThat(v1).isEqualTo(v2);
    }

}
