package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
public class BTupleTest {
    @Test
    public void testComponents() throws TranslationException {
        final BTuple<BNumber, BNumber> t1
                = Translator.translate("(1 |-> 2)");
        assertThat(t1.getFirst()).isEqualTo(new BNumber(1));
        assertThat(t1.getSecond()).isEqualTo(new BNumber(2));
    }

    @Test
    public void testComponents2() throws TranslationException {
        final BTuple<BNumber, BNumber> t1
                = Translator.translate("(1, 2)");
        assertThat(t1.getFirst()).isEqualTo(new BNumber(1));
        assertThat(t1.getSecond()).isEqualTo(new BNumber(2));
    }

    @Test
    public void testNested() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> result
                = Translator.translate("(a |-> 1 |-> b)");
        assertThat(result.getFirst().getFirst().stringValue()).isEqualTo("a");
        assertThat(result.getFirst().getSecond().intValue()).isEqualTo(1);
        assertThat(result.getSecond().stringValue()).isEqualTo("b");
    }

    @Test
    public void testManualNested() throws TranslationException {
        final BTuple<BAtom, BTuple<BNumber, BAtom>> result
                = Translator.translate("(a |-> (1 |-> b))");
        assertThat(result.getFirst().stringValue()).isEqualTo("a");
        assertThat(result.getSecond().getFirst().intValue()).isEqualTo(1);
        assertThat(result.getSecond().getSecond().stringValue()).isEqualTo("b");
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testNestedTyping() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> result2
                = Translator.translate("(99 |-> 1 |-> b)");
        final BNumber actual
                = ((BNumber) ((Object) result2.getFirst().getFirst()));
        final int expected = 99;
        assertThat(actual.intValue()).isEqualTo(expected);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testNestedTyping2() throws TranslationException {
        final BTuple<BTuple<BAtom, BNumber>, BAtom> tuple
                = Translator.translate("(99 |-> 1 |-> b)");
        assertThrows(ClassCastException.class,
                () -> tuple.getFirst().getFirst().stringValue());
    }

    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void testCoupleWith3Children() throws TranslationException {
        final BTuple<BTuple<BNumber, BNumber>, BNumber> tuple
                = Translator.translate("(1,2,3)");
        final BTuple<BNumber, BNumber> left = tuple.getFirst();
        assertThat(left.getFirst().intValue()).isEqualTo(1);
        assertThat(left.getSecond().intValue()).isEqualTo(2);
        assertThat(tuple.getSecond().intValue()).isEqualTo(3);
    }

    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void testMapletWith3Children() throws TranslationException {
        final BTuple<BTuple<BNumber, BNumber>, BNumber> tuple
                = Translator.translate("(1 |-> 2 |-> 3)");
        final BTuple<BNumber, BNumber> left = tuple.getFirst();
        assertThat(left.getFirst().intValue()).isEqualTo(1);
        assertThat(left.getSecond().intValue()).isEqualTo(2);
        assertThat(tuple.getSecond().intValue()).isEqualTo(3);
    }

    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void testCoupleWith4Children() throws TranslationException {
        final BTuple<BTuple<BTuple<BNumber, BNumber>, BNumber>, BNumber> tuple
                = Translator.translate("(1,2,3,4)");
        final BTuple<BTuple<BNumber, BNumber>, BNumber> left = tuple.getFirst();
        final BTuple<BNumber, BNumber> lefter = left.getFirst();
        assertThat(lefter.getFirst().intValue()).isEqualTo(1);
        assertThat(lefter.getSecond().intValue()).isEqualTo(2);
        assertThat(left.getSecond().intValue()).isEqualTo(3);
        assertThat(tuple.getSecond().intValue()).isEqualTo(4);
    }

    @SuppressWarnings("checkstyle:magicnumber")
    @Test
    public void testMapletWith4Children() throws TranslationException {
        final BTuple<BTuple<BTuple<BNumber, BNumber>, BNumber>, BNumber> tuple
                = Translator.translate("(1|->2|->3|->4)");
        final BTuple<BTuple<BNumber, BNumber>, BNumber> left = tuple.getFirst();
        final BTuple<BNumber, BNumber> lefter = left.getFirst();
        assertThat(lefter.getFirst().intValue()).isEqualTo(1);
        assertThat(lefter.getSecond().intValue()).isEqualTo(2);
        assertThat(left.getSecond().intValue()).isEqualTo(3);
        assertThat(tuple.getSecond().intValue()).isEqualTo(4);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BTuple.class)
                .withNonnullFields("first", "second") // fields are final
                .verify();
    }
}
