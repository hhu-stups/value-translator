package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({
        "PMD.BeanMembersShouldSerialize",
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
        assertThat(atom).isEqualTo(new BAtom(ATOM));
    }

    @Test
    public void testNotEquals() {
        assertThat(atom).isNotEqualTo(new BAtom("other"));
    }

    @Test
    public void testStringValue() {
        assertThat(atom.stringValue()).isEqualTo(ATOM);
    }
}
