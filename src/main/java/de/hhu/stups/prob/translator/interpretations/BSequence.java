package de.hhu.stups.prob.translator.interpretations;

import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.exceptions.RepeatedKeyException;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: hide classes from main interface
public class BSequence<V extends BValue> extends BFunction<BNumber, V>{
    @SuppressWarnings("unchecked")
    public BSequence(final Set<? extends BValue> bValues) {
        super(bValues);
        final boolean isValid = bValues.stream().allMatch(t -> {
            if (!t.getClass().equals(BTuple.class)) {
                return false;
            }
            return ((BTuple<BValue, ?>) t).first().getClass().equals(BNumber.class);
        });
        if (!isValid) {
            throw new RuntimeException("Incompatible set for conversion to sequence");
        }
    }

    /**
     * Converts a sequence-type set (pairs of index, value) to a list of values ordered by their index.
     *
     * @return list of values
     */
    @SuppressWarnings("unchecked")
    public List<V> toList() {
        return this.toList(Function.identity());
    }

    public <K> List<K> toList(final Function<V, K> mapper) {
        final Set<BNumber> seen = new HashSet<>();
        return this.values.stream()
                       .peek(t -> {
                           if (!seen.add(t.first())) {
                               throw new RepeatedKeyException(
                                       String.format("Repeated Key in Sequence: key=%s", t.first()));
                           }
                       })
                       .sorted(Comparator.comparingInt(value -> value.first().intValue()))
                       .map(BTuple::second)
                       .map(mapper)
                       .collect(
                               Collectors.collectingAndThen(
                                       Collectors.toList(),
                                       Collections::unmodifiableList));
    }

}
