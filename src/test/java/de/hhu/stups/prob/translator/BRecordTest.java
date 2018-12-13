package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class BRecordTest{

    @Test
    public void parseRecord() throws BCompoundException {
        final BRecord record = Translator.translate("rec(a:1)");
        assertNotNull(record);
    }

    @Test
    public void recordToMap() throws BCompoundException {
        final BRecord record = Translator.translate("rec(a:1, b: x)");
        final Map<String, BValue> map = record.toMap();
        Assert.assertTrue(map.containsKey("a"));
        Assert.assertTrue(map.containsKey("b"));
    }

    @Test
    public void recordToMapFieldTypes() throws BCompoundException {
        final BRecord record = Translator.translate("rec(a:1, b: x)");
        final Map<String, BValue> map = record.toMap();
        Assert.assertEquals(BNumber.class, map.get("a").getClass());
        Assert.assertEquals(BAtom.class, map.get("b").getClass());
    }

    @Test(expected = BCompoundException.class)
    public void testInvalidKey() throws BCompoundException {
        Translator.translate("rec(1:a)");
    }
}
