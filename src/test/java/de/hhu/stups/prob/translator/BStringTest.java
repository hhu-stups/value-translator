package de.hhu.stups.prob.translator;


import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class BStringTest {

    private static BString str1;
    private static BString str2;

    @BeforeAll
    public static void setUp() throws TranslationException {
        str1 = Translator.translate("\"lorem ipsum\"");
        str2 = Translator.translate("\"\"");
    }

    @Test
    public void testGetValue() {
        assertThat(str1.stringValue()).isEqualTo("lorem ipsum");
        assertThat(str2.stringValue()).isEqualTo("");
    }

    @Test
    public void testEqualsObject() throws TranslationException {
        final BString other = Translator.translate("\"lorem ipsum\"");
        assertThat(str1).isEqualTo(other);
    }

    @Test
    public void testToString() {
        assertThat(str1.toString()).isEqualTo("\"lorem ipsum\"");
        assertThat(str2.toString()).isEqualTo("\"\"");
    }
}
