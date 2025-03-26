package de.hhu.stups.prob.translator.interpretations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("checkstyle:magicnumber")
public class BRelationTest {

    @Test
    public void relationToMap() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (2, b), (3,c)}");
        final Map<BNumber, List<BAtom>> map
                = set.asRelation(BNumber.class, BAtom.class).toRelationalMap();
        assertEquals(Collections.singletonList(
                BValue.atom("a")), map.get(BValue.number(1)));
        assertEquals(Collections.singletonList(
                BValue.atom("b")), map.get(BValue.number(2)));
        assertEquals(Collections.singletonList(
                BValue.atom("c")), map.get(BValue.number(3)));
    }

    @Test
    public void relationToMapDuplicateKeys() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (3,c)}");
        final Map<BNumber, List<BAtom>> map
                = set.asRelation(BNumber.class, BAtom.class).toRelationalMap();

        assertThat(Stream.of("a", "b")
                           .map(BValue::atom)
                           .collect(Collectors.toList()))
                .isEqualTo(map.get(BValue.number(1)));
        assertEquals(Collections.singletonList(BValue.atom("c")),
                map.get(BValue.number(3)));
    }

    @Test
    public void functionToMapWithExtractorDuplicateKeys()
            throws TranslationException {

        final BSet<BTuple<BNumber, BAtom>> set
            = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BRelation<BNumber, BAtom> relation = set.asRelation();
        final Map<Integer, List<String>> map
            = relation.toRelationalMap(BNumber::intValue,
            BAtom::stringValue);
        assertThat(map.get(1)).isEqualTo(Arrays.asList("a", "b"));
        assertThat(map.get(3)).isEqualTo(Collections.singletonList("c"));
    }

    @Test
    public void newRelation() throws TranslationException {
        final BSet<BNumber> set = Translator.translate("{1,2,3}");
        assertThrows(InterpretationException.class, set::asRelation);
    }

    @Test
    public void newFunction2() throws TranslationException {
        final BSet<BValue> set
                = Translator.translate("{(1 |-> 2 |-> 3), (2 |-> 3 |-> 4)}");
        final BRelation<BTuple<BNumber, BNumber>, BNumber> function
                = set.asRelation();
        final Map<Long, List<Long>> map = function.toRelationalMap(
                tuple -> tuple.getFirst().longValue()
                                 + tuple.getSecond().longValue(),
                BNumber::longValue);
        assertThat(map.get(3L)).isEqualTo(Collections.singletonList(3L));
        assertThat(map.get(5L)).isEqualTo(Collections.singletonList(4L));

    }

    @Test
    public void functionToMapExtractValues() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (2, b), (3,c)}");

        final Map<Integer, String> map
                = set.asFunction(BNumber.class, BAtom.class)
                          .toMap(BNumber::intValue, BAtom::stringValue);

        assertThat(map.get(1)).isEqualTo("a");
        assertThat(map.get(2)).isEqualTo("b");
        assertThat(map.get(3)).isEqualTo("c");
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BRelation.class)
                .withNonnullFields("values") // field is final
                .verify();
    }
}
