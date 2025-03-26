package de.hhu.stups.prob.translator.interpretations;

import java.util.Map;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.DuplicateKeyException;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;
import de.hhu.stups.prob.translator.exceptions.TranslationException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("checkstyle:magicnumber")
public class BFunctionTest {

    @Test
    public void functionToMap() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (2, b), (3,c)}");
        final Map<BNumber, BAtom> map
                = set.asFunction(BNumber.class, BAtom.class).toMap();
        assertThat(map.get(BValue.number(1))).isEqualTo(BValue.atom("a"));
        assertThat(map.get(BValue.number(2))).isEqualTo(BValue.atom("b"));
        assertThat(map.get(BValue.number(3))).isEqualTo(BValue.atom("c"));
    }

    @Test
    public void functionToMapDuplicateKeys() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (3,c)}");
        assertThrows(DuplicateKeyException.class, set::asFunction);
    }

    @Test
    public void newFunction() throws TranslationException {
        final BSet<BNumber> set = Translator.translate("{1,2,3}");
        assertThrows(InterpretationException.class, set::asFunction);
    }

    @Test
    public void newFunction2() throws TranslationException {
        final BSet<?> set
                = Translator.translate("{(1 |-> 2 |-> 3), (2 |-> 3 |-> 4)}");
        final BFunction<BTuple<BNumber, BNumber>, BNumber> function
                = set.asFunction();
        final Map<Long, Long> map = function.toMap(
                tuple -> tuple.getFirst().longValue()
                                 + tuple.getSecond().longValue(),
                BNumber::longValue);
        assertThat(map.get(3L)).isEqualTo(Long.valueOf(3));
        assertThat(map.get(5L)).isEqualTo(Long.valueOf(4));

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
    public void translateToFunction() {
        assertThrows(ClassCastException.class, () -> {
            @SuppressWarnings("unused")
            final BFunction<BNumber, BAtom> set =
                    Translator.translate("{(1,a), (2, b), (3,c)}");
        });
    }

    @Test
    public void translateToSetOfFunctions() throws TranslationException {
        final BSet<BFunction<BNumber, BAtom>> set
                = Translator.translate("{{(1,a), (2, b), (3,c)}}");
        assertThrows(ClassCastException.class, () -> {
            @SuppressWarnings("unused")
            final BFunction<BNumber, BAtom> func
                    = set.toSet().iterator().next();
        });
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BFunction.class)
                .withNonnullFields("values") // field is final
                .verify();
    }

}
