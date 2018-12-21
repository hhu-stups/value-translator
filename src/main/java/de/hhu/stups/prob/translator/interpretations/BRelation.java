package de.hhu.stups.prob.translator.interpretations;

import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class BRelation<K extends BValue, V extends BValue>
        extends BSet<BTuple<K, V>> {
    @SuppressWarnings("unchecked")
    public BRelation(final Set<? extends BValue> bValues) {
        super((Set<BTuple<K, V>>) bValues);
        final boolean isValid
                = bValues.stream()
                          .allMatch(
                                  t -> t.getClass().equals(BTuple.class));
        if (!isValid) {
            throw new RuntimeException(
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
                       .map(tuple -> new Pair<>(keyMapper.apply(tuple.first()),
                               valueMapper.apply(tuple.second()))).collect(
                        Collectors.groupingBy(o -> o.key,
                                Collectors.mapping(o -> o.value,
                                        Collectors.collectingAndThen(
                                                Collectors.toList(),
                                                Collections::unmodifiableList
                                        ))));
    }

    private static final class Pair<M, N> {
        private final M key;
        private final N value;

        Pair(final M m, final N n) {
            super();
            this.key = m;
            this.value = n;
        }
    }
}
