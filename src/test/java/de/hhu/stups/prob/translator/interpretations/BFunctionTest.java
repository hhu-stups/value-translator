package de.hhu.stups.prob.translator.interpretations;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.DuplicateKeyException;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;
import de.hhu.stups.prob.translator.exceptions.TranslationException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        assertThat(map.get(new BNumber(1))).isEqualTo(new BAtom("a"));
        assertThat(map.get(new BNumber(2))).isEqualTo(new BAtom("b"));
        assertThat(map.get(new BNumber(3))).isEqualTo(new BAtom("c"));

    }

    @Test
    public void functionToMapDuplicateKeys() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BFunction<BNumber, BAtom> function = set.asFunction();
        assertThrows(DuplicateKeyException.class, function::toMap);
    }

    @Test
    public void functionToMapWithExtractorDuplicateKeys()
            throws TranslationException {

        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BFunction<BNumber, BAtom> function = set.asFunction();
        assertThrows(DuplicateKeyException.class,
                () -> function.toMap(BNumber::intValue, BAtom::stringValue));
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

    @SuppressWarnings({"unused", "PMD.DataflowAnomalyAnalysis"})
    @SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE",
            justification = "Type of the variable is needed to trigger "
                                    + "a ClassCastException.")
    @Test
    public void translateToFunction() {
        assertThrows(ClassCastException.class, () -> {
            final BFunction<BNumber, BAtom> set =
                    Translator.translate("{(1,a), (2, b), (3,c)}");
        });
    }

    @SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE",
            justification = "Type of the variable is needed to trigger "
                                    + "a ClassCastException.")
    @SuppressWarnings({"unused", "PMD.DataflowAnomalyAnalysis"})
    @Test
    public void translateToSetOfFunctions() throws TranslationException {
        final BSet<BFunction<BNumber, BAtom>> set
                = Translator.translate("{{(1,a), (2, b), (3,c)}}");
        assertThrows(ClassCastException.class, () -> {
            final BFunction<BNumber, BAtom> func
                    = set.toSet().iterator().next();
        });
    }

}
