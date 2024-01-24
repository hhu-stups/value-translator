package de.hhu.stups.prob.translator.interpretations;

import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class BRelation<K extends BValue, V extends BValue>
        extends BSet<BTuple<K, V>> {
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    @SuppressWarnings("unchecked")
    public BRelation(final Set<? extends BValue> bValues) {
        super((Set<BTuple<K, V>>) bValues);
        final boolean isValid
                = bValues.stream()
                          .allMatch(
                                  value -> value.getClass()
                                                   .equals(BTuple.class));
        if (!isValid) {
            throw new InterpretationException(
                    "Incompatible set for conversion to relation/function");
        }
    }

    public Map<K, List<V>> toRelationalMap() {
        return this.toRelationalMap(Function.identity(), Function.identity());
    }

    public <M, N> Map<M, List<N>> toRelationalMap(
            final Function<K, M> keyMapper, final Function<V, N> valueMapper) {

        return this.getValues()
                       .stream()
                       .map(tuple -> new Pair<>(
                               keyMapper.apply(tuple.getFirst()),
                               valueMapper.apply(tuple.getSecond())
                       )).collect(
                        Collectors.groupingBy(Pair::getKey,
                                Collectors.mapping(Pair::getValue,
                                        Collectors.collectingAndThen(
                                                Collectors.toList(),
                                                Collections::unmodifiableList
                                        ))));
    }

    @SuppressWarnings("PMD.ShortClassName")
    /* default */ static final class Pair<M, N> {
        private final M key;
        private final N value;

        /* default */ Pair(final M mKey, final N nValue) {
            super();
            this.key = mKey;
            this.value = nValue;
        }

        public M getKey() {
            return this.key;
        }

        public N getValue() {
            return this.value;
        }
    }
}
