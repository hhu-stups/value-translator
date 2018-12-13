package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class BSetTest{
    private BSet<BNumber> set1;
    private BSet<BValue> set2;

    @Before
    public void setUp() throws Exception {
        this.set1 = Translator.translate("{1,2,3}");
        this.set2 = Translator.translate("{}");
    }


    @Test
    public void testSize() {
        assertEquals(3, this.set1.toSet().size());
        assertTrue(this.set2.toSet().isEmpty());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test(expected = ClassCastException.class)
    public void testCast() throws BCompoundException {
        final BSet<BNumber> bSet = Translator.translate("{a,b,c}");
        bSet.toSet().stream().map(BNumber::intValue).collect(Collectors.toList());
    }

    @Test(expected = ClassCastException.class)
    public void testCastInNestedSet() throws BCompoundException {
        final BSet<BSet<BNumber>> nestedSet = Translator.translate("{{a,b,c}}");

        final Set<BSet<BNumber>> sets = nestedSet.toSet();
        assertEquals(1, sets.size());
        final List<BNumber> number
                = sets.stream()
                          .flatMap(bNumberBSet -> bNumberBSet.toSet().stream())
                          .collect(Collectors.toList());
        assertEquals(3, number.size());

        //noinspection ResultOfMethodCallIgnored
        number.stream().mapToInt(BNumber::intValue).sum();
    }

    @Test
    public void testToSet() throws BCompoundException {
        final BSet<BNumber> x = Translator.translate("{3,2,1}");
        final Set<BNumber> expected = Stream.of(1, 2, 3).map(BNumber::new).collect(Collectors.toSet());
        assertEquals(expected, x.toSet());
    }

    @Test
    public void testToStream() throws BCompoundException {
        final BSet<BNumber> x = Translator.translate("{3,2,1}");
        final Set<BNumber> expected = Stream.of(1, 2, 3).map(BNumber::new).collect(Collectors.toSet());
        final Set<BNumber> result = x.stream().collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}
