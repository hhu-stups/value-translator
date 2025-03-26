package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({
        "JUnitAssertionsShouldIncludeMessage",
        "AtLeastOneConstructor"})
public class BAtomTest {

    private static final String ATOM = "atom";
    private static BAtom atom;

    @BeforeAll
    public static void setUp() throws TranslationException {
        BAtomTest.atom = Translator.translate(ATOM);
    }

    @Test
    public void testToString() {
        assertThat(String.valueOf(atom)).isEqualTo(ATOM);
    }

    @Test
    public void testEquals() {
        assertThat(atom).isEqualTo(BValue.atom(ATOM));
    }

    @Test
    public void testNotEquals() {
        assertThat(atom).isNotEqualTo(BValue.atom("other"));
    }

    @Test
    public void testStringValue() {
        assertThat(atom.stringValue()).isEqualTo(ATOM);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BAtom.class)
                .withNonnullFields("value") // field is final
                .verify();
    }
}
