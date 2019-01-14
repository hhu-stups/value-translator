package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"MagicNumber", "FeatureEnvy"})
public class BNumberTest {

    @Test
    public void testNumbers() throws TranslationException {
        final BNumber one = Translator.translate("1");
        final BNumber two = Translator.translate("2");
        assertEquals(1, one.intValue());
        assertEquals(2.0, two.doubleValue());
        assertEquals(1L, one.longValue());
    }

    @SuppressWarnings({"unused", "PMD.DataflowAnomalyAnalysis"})
    @Test
    @SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE",
            justification = "Type of the variable is needed to"
                                    + " trigger a ClassCastException.")
    public void testNumberCast() {
        assertThrows(ClassCastException.class, () -> {
            final BString string = Translator.translate("1");
        });
    }

    @Test
    public void testNumberCastUp() throws TranslationException {
        final BValue value = Translator.translate("1");
        assertSame(BNumber.class, value.getClass());
    }

    @Test
    public void testNegativeNumber() throws TranslationException {
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

    @Test
    @DisplayName("Only numeric values that can be represented as a"
                         + " Long are supported")
    public void testNumericRangeIsLong() throws TranslationException {
        assertEquals(Long.MAX_VALUE,
                Translator.<BNumber>translate(
                        String.valueOf(Long.MAX_VALUE)).longValue());
        assertEquals(Long.MIN_VALUE,
                Translator.<BNumber>translate(
                        String.valueOf(Long.MIN_VALUE)).longValue());

        final String tooLong = new BigDecimal(Long.MAX_VALUE)
                                       .add(new BigDecimal(1))
                                       .toPlainString();
        assertThrows(NumberFormatException.class,
                () -> Translator.<BNumber>translate(tooLong));
    }
}
