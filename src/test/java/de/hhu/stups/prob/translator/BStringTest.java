package de.hhu.stups.prob.translator;


import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("lorem ipsum", str1.stringValue());
        assertEquals("", str2.stringValue());
    }

    @Test
    public void testEqualsObject() throws TranslationException {
        final BString other = Translator.translate("\"lorem ipsum\"");
        assertEquals(other, str1);
    }

    @Test
    public void testToString() {
        assertEquals("\"lorem ipsum\"", str1.toString());
        assertEquals("\"\"", str2.toString());
    }
}
