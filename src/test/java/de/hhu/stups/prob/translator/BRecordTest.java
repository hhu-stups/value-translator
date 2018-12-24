package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BRecordTest {

    @Test
    public void parseRecord() throws TranslationException {
        final BRecord record = Translator.translate("rec(a:1)");
        assertNotNull(record);
    }

    @Test
    public void recordToMap() throws TranslationException {
        final BRecord record = Translator.translate("rec(a:1, b: x)");
        final Map<String, BValue> map = record.toMap();
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("b"));
    }

    @Test
    public void recordToMapFieldTypes() throws TranslationException {
        final BRecord record = Translator.translate("rec(a:1, b: x)");
        final Map<String, BValue> map = record.toMap();
        assertEquals(BNumber.class, map.get("a").getClass());
        assertEquals(BAtom.class, map.get("b").getClass());
    }

    @Test
    public void testInvalidKey() {
        assertThrows(TranslationException.class,
                () -> Translator.translate("rec(1:a)"));
    }
}
