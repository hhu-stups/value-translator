package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BRecordTest {

    @Test
    public void parseRecord() throws TranslationException {
        final BRecord record = Translator.translate("rec(a:1)");
        assertThat(record).isNotNull();
    }

    @Test
    public void recordToMap() throws TranslationException {
        final BRecord record = Translator.translate("rec(a:1, b: x)");
        final Map<String, BValue> map = record.toMap();
        assertThat(map.containsKey("a")).isTrue();
        assertThat(map.containsKey("b")).isTrue();
    }

    @Test
    public void recordToMapFieldTypes() throws TranslationException {
        final BRecord record = Translator.translate("rec(a:1, b: x)");
        final Map<String, BValue> map = record.toMap();
        assertThat(map.get("a")).isInstanceOf(BNumber.class);
        assertThat(map.get("b").getClass()).isEqualTo(BAtom.class);
    }

    @Test
    public void testInvalidKey() {
        assertThrows(TranslationException.class,
                () -> Translator.translate("rec(1:a)"));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BRecord.class)
                .withNonnullFields("values") // field is final
                .verify();
    }
}
