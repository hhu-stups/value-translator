package de.hhu.stups.prob.translator;

import java.math.BigInteger;

import de.hhu.stups.prob.translator.exceptions.TranslationException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({ "MagicNumber", "FeatureEnvy" })
public class BNumberTest {

    @Test
    public void testNumbers() throws TranslationException {
        final BNumber one = Translator.translate("1");
        final BNumber two = Translator.translate("2");
        assertThat(one.intValue()).isEqualTo(1);
        assertThat(two.doubleValue()).isEqualTo(2.0);
        assertThat(one.longValue()).isEqualTo(1L);
    }

    @Test
    public void testNumberCast() {
        assertThrows(ClassCastException.class, () -> {
            @SuppressWarnings("unused")
            final BString string = Translator.translate("1");
        });
    }

    @Test
    public void testNumberCastUp() throws TranslationException {
        final BValue value = Translator.translate("1");
        assertThat(value).isInstanceOf(BNumber.class);
    }

    @Test
    public void testNegativeNumber() throws TranslationException {
        final BNumber number = Translator.translate("-1");
        assertThat(number.intValue()).isEqualTo(-1);
    }

    @Test
    public void testEquality() {
        final BNumber one = BNumber.of(1);
        final BNumber two = BNumber.of(1);
        assertThat(two).isEqualTo(one);
    }

    @Test
    public void testEqualityDifferentTypeArguments() {
        final BNumber one = BNumber.of(1);
        final BNumber two = BNumber.of(1L);
        assertThat(two).isEqualTo(one);
    }

    @Test
    public void testBigIntegerSupported() throws TranslationException {
        BigInteger tooLong = BigInteger.valueOf(Long.MAX_VALUE)
                                 .multiply(BigInteger.valueOf(3));
        BNumber translated = Translator.translate(tooLong.toString());
        assertThat(translated.bigIntegerValue()).isEqualTo(tooLong);
    }

    @Test
    @Disabled("abstract super class not supported by EqualsVerifier")
    public void equalsContractSmall() {
        EqualsVerifier.forClass(BSmallNumber.class)
            .withNonnullFields("value") // field is final
            .verify();
    }

    @Test
    public void equalsContractBig() {
        EqualsVerifier.forClass(BBigNumber.class)
            .withNonnullFields("value") // field is final
            .verify();
    }

    @Test
    public void hashCodeContract1() {
        BSmallNumber n1 = new BSmallNumber(1);
        BBigNumber n2 = new BBigNumber(BigInteger.ONE);
        assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
    }

    @Test
    public void hashCodeContract2() {
        BSmallNumber n1 = new BSmallNumber(-1);
        BBigNumber n2 = new BBigNumber(BigInteger.valueOf(-1));
        assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
    }
}
