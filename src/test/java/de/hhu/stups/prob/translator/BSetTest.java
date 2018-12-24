package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings( {"checkstyle:magicnumber", "PMD.BeanMembersShouldSerialize"})
public class BSetTest {
    private static BSet<BNumber> set1;
    private static BSet<BValue> set2;

    @BeforeAll
    public static void setUp() throws TranslationException {
        set1 = Translator.translate("{1,2,3}");
        set2 = Translator.translate("{}");
    }


    @Test
    public void testSize() {
        final int setSize = 3;
        assertEquals(setSize, set1.toSet().size());
        assertTrue(set2.toSet().isEmpty());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testCast() throws TranslationException {
        final BSet<BNumber> bSet = Translator.translate("{a,b,c}");
        assertThrows(ClassCastException.class,
                () -> bSet.toSet().stream()
                              .map(BNumber::intValue)
                              .collect(Collectors.toList()));
    }

    @Test
    public void testCastInNestedSet() throws TranslationException {
        final BSet<BSet<BNumber>> nestedSet = Translator.translate("{{a,b,c}}");

        final Set<BSet<BNumber>> sets = nestedSet.toSet();
        assertEquals(1, sets.size());
        final List<BNumber> number
                = sets.stream()
                          .flatMap(bNumberBSet -> bNumberBSet.toSet().stream())
                          .collect(Collectors.toList());
        final int setSize = 3;
        assertEquals(setSize, number.size());

        //noinspection ResultOfMethodCallIgnored
        assertThrows(ClassCastException.class,
                () -> number.stream().mapToInt(BNumber::intValue).sum());
    }

    @Test
    @SuppressWarnings("CheckStyle")
    public void testToSet() throws TranslationException {
        final BSet<BNumber> x = Translator.translate("{3,2,1}");
        final Set<BNumber> expected
                = Stream.of(1, 2, 3)
                          .map(BNumber::new)
                          .collect(Collectors.toSet());
        assertEquals(expected, x.toSet());
    }

    @Test
    public void testToStream() throws TranslationException {
        final BSet<BNumber> x = Translator.translate("{3,2,1}");
        //noinspection CheckStyle
        final Set<BNumber> expected
                = Stream.of(1, 2, 3)
                          .map(BNumber::new)
                          .collect(Collectors.toSet());
        final Set<BNumber> result = x.stream().collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}
