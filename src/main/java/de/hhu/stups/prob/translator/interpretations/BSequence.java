package de.hhu.stups.prob.translator.interpretations;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;

@SuppressWarnings("PMD.ShortMethodName")
public final class BSequence<V extends BValue> extends BFunction<BNumber, V> {

    private BSequence(final Set<? extends BValue> bValues) {
        super(bValues);
    }

    public static <T extends BValue> BSequence<T> of() {
        return new BSequence<>(Collections.emptySet());
    }

    public static <T extends BValue> BSequence<T> of(
        final List<? extends T> values) {

        final Set<BTuple<BNumber, ?>> set
            = IntStream
                  .range(0, values.size())
                  .mapToObj(index -> new BTuple<>(
                      BNumber.of(index + 1), values.get(index)))
                  .collect(Collectors.toSet());
        return new BSequence<>(set);
    }

    public static <T extends BValue> BSequence<T> fromBValues(
        final Set<? extends BValue> values) {

        check(values);
        return new BSequence<>(values);
    }

    /* default */
    static void check(final Set<? extends BValue> values) {
        BFunction.check(values);
        final int size = values.size();
        for (final BValue value : values) {
            final BValue key = ((BTuple<?, ?>) value).getFirst();
            if (!(key instanceof BNumber)) {
                throw new InterpretationException(String.format(
                    Locale.ROOT,
                    "Incompatible set for conversion to sequence: "
                        + "key is not an integer: %s",
                    value
                ));
            }
            int index;
            try {
                index = ((BNumber) key).intValueExact();
            } catch (ArithmeticException e) {
                index = 0; // to throw later
            }
            if (index < 1 || index > size) {
                throw new InterpretationException(String.format(
                    Locale.ROOT,
                    "Incompatible set for conversion to sequence: "
                        + "integer key is of bounds: %s",
                    key
                ));
            }
        }
    }

    /**
     * Converts a sequence-type set (pairs of index, value) to a list of values
     * ordered by their index.
     *
     * @return list of values
     */
    public List<V> toList() {
        return this.toList(Function.identity());
    }

    public <K> List<K> toList(final Function<V, K> mapper) {
        final Comparator<BTuple<BNumber, V>> keyComparator =
            Comparator.comparingInt(value -> value.getFirst().intValue());
        return this.stream()
                   .sorted(keyComparator)
                   .map(BTuple::getSecond)
                   .map(mapper)
                   .collect(Collectors.collectingAndThen(
                       Collectors.toList(),
                       Collections::unmodifiableList
                   ));
    }

    @Override
    public String toString() {
        return this.toList().stream()
                   .map(Object::toString)
                   .collect(Collectors.joining(", ", "[", "]"));
    }
}
