package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

public class BRelationTest{
    @Test
    public void relationToMap() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (2, b), (3,c)}");
        final Map<BNumber, List<BAtom>> map = set.asRelation(BNumber.class, BAtom.class).toRelationalMap();
        assertEquals(Collections.singletonList(new BAtom("a")), map.get(new BNumber(1)));
        assertEquals(Collections.singletonList(new BAtom("b")), map.get(new BNumber(2)));
        assertEquals(Collections.singletonList(new BAtom("c")), map.get(new BNumber(3)));

    }

    @Test
    public void relationToMapDuplicateKeys() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (3,c)}");
        final Map<BNumber, List<BAtom>> map = set.asRelation(BNumber.class, BAtom.class).toRelationalMap();

        assertEquals(Stream.of("a", "b").map(BAtom::new).collect(Collectors.toList()), map.get(new BNumber(1)));
        assertEquals(Collections.singletonList(new BAtom("c")), map.get(new BNumber(3)));
    }

    @Test
    public void functionToMapWithExtractorDuplicateKeys() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BRelation<BNumber, BAtom> function = set.asFunction();
        final Map<Integer, List<String>> map = function.toRelationalMap(BNumber::intValue, BAtom::stringValue);
        assertEquals(Arrays.asList("a", "b"), map.get(1));
        assertEquals(Collections.singletonList("c"), map.get(3));
    }

    @Test(expected = RuntimeException.class) // TODO: custom exception
    public void newRelation() throws BCompoundException {
        final BSet<BNumber> set = Translator.translate("{1,2,3}");
        set.asRelation();
    }

    @Test
    public void newFunction2() throws BCompoundException {
        final BSet<BValue> set = Translator.translate("{(1 |-> 2 |-> 3), (2 |-> 3 |-> 4)}");
        final BRelation<BTuple<BNumber, BNumber>, BNumber> function = set.asRelation();
        final Map<Long, List<Long>> map = function.toRelationalMap(
                tuple -> tuple.first().longValue() + tuple.second().longValue(),
                BNumber::longValue);
        assertEquals(Collections.singletonList(3L), map.get(3L));
        assertEquals(Collections.singletonList(4L), map.get(5L));

    }

    @Test
    public void functionToMapExtractValues() throws BCompoundException {
        final BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (2, b), (3,c)}");

        final Map<Integer, String> map
                = set.asFunction(BNumber.class, BAtom.class)
                          .toMap(BNumber::intValue, BAtom::stringValue);

        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }

}
