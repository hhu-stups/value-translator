package de.hhu.stups.prob.translator;

import de.hhu.stups.prob.translator.exceptions.TranslationException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"checkstyle:magicnumber", "PMD.BeanMembersShouldSerialize"})
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
        assertThat(set1.toSet().size()).isEqualTo(setSize);
        assertThat(set2.toSet().isEmpty()).isTrue();
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
        assertThat(sets).hasSize(1);
        final List<BNumber> number
                = sets.stream()
                          .flatMap(bNumberBSet -> bNumberBSet.toSet().stream())
                          .collect(Collectors.toList());
        final int setSize = 3;
        assertThat(number.size()).isEqualTo(setSize);

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
        assertThat(x.toSet()).isEqualTo(expected);
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
        assertThat(result).isEqualTo(expected);
    }


    @Test
    public void testStream2() throws TranslationException {
        final BSet<BNumber> x = Translator.translate("{1,2,3,2}");
        final int result = x.stream().mapToInt(BNumber::intValue).sum();
        assertThat(result).isEqualTo(6);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BSet.class)
                .withNonnullFields("values") // field is final
                .verify();
    }
}
