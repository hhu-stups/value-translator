package de.hhu.stups.prob.translator.interpretations;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.hhu.stups.prob.translator.BSet;
import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.exceptions.InterpretationException;

public class BRelation<K extends BValue, V extends BValue>
        extends BSet<BTuple<K, V>> {

    /* default */ BRelation(final Set<BTuple<K, V>> bValues) {
        super(bValues);
    }

    @SuppressWarnings("unchecked")
    public static <K extends BValue, V extends BValue> BRelation<K, V>
    relationFromBValues(final Set<? extends BValue> values) {
        check(values);
        return new BRelation<>((Set<BTuple<K, V>>) values);
    }

    /* default */
    static void check(final Set<? extends BValue> values) {
        Objects.requireNonNull(values, "values");
        for (final BValue value : values) {
            if (!(value instanceof BTuple<?, ?>)) {
                throw new InterpretationException(String.format(
                    Locale.ROOT,
                    "Incompatible set for conversion to relation: "
                        + "value is not a tuple: %s",
                    value
                ));
            }
        }
    }

    public Map<K, List<V>> toRelationalMap() {
        return this.toRelationalMap(Function.identity(), Function.identity());
    }

    public <M, N> Map<M, List<N>> toRelationalMap(
            final Function<K, M> keyMapper, final Function<V, N> valueMapper) {
        return this.stream()
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
