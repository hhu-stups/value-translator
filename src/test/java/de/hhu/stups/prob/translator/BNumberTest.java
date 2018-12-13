package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertSame;

@SuppressWarnings({"MagicNumber", "FeatureEnvy"})
public class BNumberTest{

    @Test
    public void testNumbers() throws BCompoundException {
        final BNumber one = Translator.translate("1");
        final BNumber two = Translator.translate("2");
        assertEquals(1, one.intValue());
        assertEquals(2.0, two.doubleValue());
        assertEquals(1L, one.longValue());
    }

    @SuppressWarnings("unused")
    @Test(expected = ClassCastException.class)
    @SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE",
            justification = "Type of the variable is needed to trigger a ClassCastException.")
    public void testNumberCast() throws BCompoundException {
        final BString string = Translator.translate("1");
    }

    @Test
    public void testNumberCastUp() throws BCompoundException {
        final BValue value = Translator.translate("1");
        assertSame(BNumber.class, value.getClass());
    }

    @Test
    public void testNegativeNumber() throws BCompoundException {
        final BNumber number = Translator.translate("-1");
        assertEquals(-1, number.intValue());
    }

    @Test
    public void testEquality() {
        final BNumber one = new BNumber(1);
        final BNumber two = new BNumber(1);
        assertEquals(one, two);
        assertNotSame(one, two);
    }
}
