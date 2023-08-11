package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"MagicNumber", "FeatureEnvy"})
public class BNumberTest {

    @Test
    public void testNumbers() throws TranslationException {
        final BNumber one = Translator.translate("1");
        final BNumber two = Translator.translate("2");
        assertThat(one.intValue()).isEqualTo(1);
        assertThat(two.doubleValue()).isEqualTo(2.0);
        assertThat(one.longValue()).isEqualTo(1L);
    }

    @SuppressWarnings({"unused", "PMD.DataflowAnomalyAnalysis"})
    @Test
    public void testNumberCast() {
        assertThrows(ClassCastException.class, () -> {
            final BString string = Translator.translate("1");
        });
    }

    @Test
    public void testNumberCastUp() throws TranslationException {
        final BValue value = Translator.translate("1");
        assertThat(value.getClass()).isSameAs(BNumber.class);
    }

    @Test
    public void testNegativeNumber() throws TranslationException {
        final BNumber number = Translator.translate("-1");
        assertThat(number.intValue()).isEqualTo(-1);
    }

    @Test
    public void testEquality() {
        final BNumber one = new BNumber(1);
        final BNumber two = new BNumber(1);
        assertThat(two).isEqualTo(one);
        assertThat(two).isNotSameAs(one);
    }

    @Test
    public void testEqualityDifferntTypeArguments() {
        final BNumber one = new BNumber(1);
        final BNumber two = new BNumber(1L);
        assertThat(two).isEqualTo(one);
        assertThat(two).isNotSameAs(one);
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

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BNumber.class)
                .withNonnullFields("value") // field is final
                .verify();
    }
}
