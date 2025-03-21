package de.hhu.stups.prob.translator.interpretations;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.hhu.stups.prob.translator.BNumber;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.exceptions.DuplicateKeyException;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class BSequence<V extends BValue> extends BFunction<BNumber, V> {
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    public BSequence(final Set<? extends BValue> bValues) {
        super(bValues);
        final boolean isValid = bValues.stream().allMatch(tuple ->
            tuple instanceof BTuple<?, ?>
            && ((BTuple<?, ?>) tuple).getFirst() instanceof BNumber
        );
        if (!isValid) {
            throw new InterpretationException(
                    "Incompatible set for conversion to sequence");
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
        final Set<BNumber> seen = new HashSet<>();
        for (final BTuple<BNumber, V> tuple : this.getValues()) {
            if (seen.add(tuple.getFirst())) {
                continue;
            }
            throw
                    new DuplicateKeyException(String.format(Locale.ROOT,
                            "Repeated Key in Sequence: key=%s",
                            tuple.getFirst()));
        }
        return this.getValues().stream()
                       .sorted(Comparator.comparingInt(
                               value -> value.getFirst().intValue()))
                       .map(BTuple::getSecond)
                       .map(mapper)
                       .collect(
                               Collectors.collectingAndThen(
                                       Collectors.toList(),
                                       Collections::unmodifiableList));
    }

}
