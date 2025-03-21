package de.hhu.stups.prob.translator.interpretations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.DuplicateKeyException;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("checkstyle:magicnumber")
public class BSequenceTest {

    @Test
    public void sequenceCreation() throws TranslationException {
        final BSequence<BNumber> sequence =
            BSequence.sequence(Arrays.asList(
                BNumber.of(3),
                BNumber.of(2),
                BNumber.of(1)
            ));
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,3), (2,2), (3,1)}");
        assertThat(sequence).isEqualTo(set);
    }

    @Test
    public void setAsSequence() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> value
                = Translator.translate("[c, b, a]");
        final BSequence<BAtom> sequence = value.asSequence();
        final Map<Integer, String> map
                = sequence.toMap(BNumber::intValue, BAtom::stringValue);
        assertThat(map.get(1)).isEqualTo("c");
        assertThat(map.get(2)).isEqualTo("b");
        assertThat(map.get(3)).isEqualTo("a");
    }

    @SuppressWarnings("FeatureEnvy")
    @Test
    public void toList2() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> value
                = Translator.translate("{(1, a), (2, b), (3, c)}");
        final List<BAtom> list = value.asSequence(BAtom.class).toList();
        assertThat(list.get(0).stringValue()).isEqualTo("a");
        assertThat(list.get(1).stringValue()).isEqualTo("b");
        assertThat(list.get(2).stringValue()).isEqualTo("c");
    }

    @Test
    public void toList() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> value
                = Translator.translate("[c, b, a]");
        final List<BAtom> list = value.asSequence(BAtom.class).toList();

        assertThat(list.get(0).stringValue()).isEqualTo("c");
        assertThat(list.get(1).stringValue()).isEqualTo("b");
        assertThat(list.get(2).stringValue()).isEqualTo("a");
    }

    @Test
    public void toListError() {
        assertThrows(
                InterpretationException.class,
                () -> Translator
                              .<BSet<?>>translate("{1,2,3}")
                              .asSequence()
                              .toList());
    }

    @Test
    public void sequenceToMapDuplicateKeys() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (2,c)}");
        assertThrows(DuplicateKeyException.class, set::asSequence);
    }

    @Test
    public void sequenceToMapWithExtractorDuplicateKeys()
            throws TranslationException {

        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (3,c)}");
        assertThrows(DuplicateKeyException.class, set::asSequence);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BSequence.class)
                .withNonnullFields("values") // field is final
                .verify();
    }
}
