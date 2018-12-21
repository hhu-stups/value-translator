package de.hhu.stups.prob.translator.interpretations;

import de.hhu.stups.prob.translator.BAtom;
import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.Translator;
import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

@SuppressWarnings("checkstyle:magicnumber")
public class BRelationTest {

    @Test
    public void relationToMap() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (2, b), (3,c)}");
        final Map<BNumber, List<BAtom>> map
                = set.asRelation(BNumber.class, BAtom.class).toRelationalMap();
        assertEquals(Collections.singletonList(
                new BAtom("a")), map.get(new BNumber(1)));
        assertEquals(Collections.singletonList(
                new BAtom("b")), map.get(new BNumber(2)));
        //noinspection CheckStyle
        assertEquals(Collections.singletonList(
                new BAtom("c")), map.get(new BNumber(3)));

    }

    @Test
    public void relationToMapDuplicateKeys() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (3,c)}");
        final Map<BNumber, List<BAtom>> map
                = set.asRelation(BNumber.class, BAtom.class).toRelationalMap();

        assertEquals(Stream.of("a", "b")
                             .map(BAtom::new)
                             .collect(Collectors.toList()),
                map.get(new BNumber(1)));
        assertEquals(Collections.singletonList(new BAtom("c")),
                map.get(new BNumber(3)));
    }

    @Test
    public void functionToMapWithExtractorDuplicateKeys()
            throws TranslationException {

        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (1, b), (3,c)}");
        final BRelation<BNumber, BAtom> function = set.asFunction();
        final Map<Integer, List<String>> map
                = function.toRelationalMap(BNumber::intValue,
                BAtom::stringValue);
        assertEquals(Arrays.asList("a", "b"), map.get(1));
        assertEquals(Collections.singletonList("c"), map.get(3));
    }

    @Test(expected = RuntimeException.class) // TODO: custom exception
    public void newRelation() throws TranslationException {
        final BSet<BNumber> set = Translator.translate("{1,2,3}");
        set.asRelation();
    }

    @Test
    public void newFunction2() throws TranslationException {
        final BSet<BValue> set
                = Translator.translate("{(1 |-> 2 |-> 3), (2 |-> 3 |-> 4)}");
        final BRelation<BTuple<BNumber, BNumber>, BNumber> function
                = set.asRelation();
        final Map<Long, List<Long>> map = function.toRelationalMap(
                tuple -> tuple.getFirst().longValue()
                                 + tuple.getSecond().longValue(),
                BNumber::longValue);
        assertEquals(Collections.singletonList(3L), map.get(3L));
        assertEquals(Collections.singletonList(4L), map.get(5L));

    }

    @Test
    public void functionToMapExtractValues() throws TranslationException {
        final BSet<BTuple<BNumber, BAtom>> set
                = Translator.translate("{(1,a), (2, b), (3,c)}");

        final Map<Integer, String> map
                = set.asFunction(BNumber.class, BAtom.class)
                          .toMap(BNumber::intValue, BAtom::stringValue);

        assertEquals("a", map.get(1));
        assertEquals("b", map.get(2));
        assertEquals("c", map.get(3));
    }

}
