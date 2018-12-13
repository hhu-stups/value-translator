package de.hhu.stups.prob.translator;


import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BStringTest{

    private BString str1;
    private BString str2;

    @Before
    public void setUp() throws Exception {
        this.str1 = Translator.translate("\"lorem ipsum\"");
        this.str2 = Translator.translate("\"\"");
    }

    @Test
    public void testGetValue() {
        assertEquals("lorem ipsum", this.str1.stringValue());
        assertEquals("", this.str2.stringValue());
    }

    @Test
    public void testEqualsObject() throws BCompoundException {
        final BString other = Translator.translate("\"lorem ipsum\"");
        assertEquals(other, this.str1);
    }

    @Test
    public void testToString() {
        assertEquals("\"lorem ipsum\"", this.str1.toString());
        assertEquals("\"\"", this.str2.toString());
    }
}
