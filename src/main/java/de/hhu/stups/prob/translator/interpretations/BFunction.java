package de.hhu.stups.prob.translator.interpretations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.hhu.stups.prob.translator.BTuple;
import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.exceptions.DuplicateKeyException;

public class BFunction<K extends BValue, V extends BValue>
        extends BRelation<K, V> {

    /* default */ BFunction(final Set<BTuple<K, V>> bValues) {
        super(bValues);
    }

    public static <K extends BValue, V extends BValue> BFunction<K, V> function(
        final Map<K, V> values) {

        final Set<BTuple<K, V>> set = values.entrySet()
                                          .stream()
                                          .map(e -> new BTuple<>(
                                              e.getKey(),
                                              e.getValue()
                                          ))
                                          .collect(Collectors.toSet());
        return new BFunction<>(set);
    }

    @SuppressWarnings("unchecked")
    public static <K extends BValue, V extends BValue> BFunction<K, V>
    functionFromBValues(final Set<? extends BValue> values) {
        check(values);
        return new BFunction<>((Set<BTuple<K, V>>) values);
    }

    /* default */
    static void check(final Set<? extends BValue> values) {
        BRelation.check(values);
        final Set<BValue> keys = new HashSet<>();
        for (final BValue value : values) {
            final BValue key = ((BTuple<?, ?>) value).getFirst();
            if (!keys.add(key)) {
                throw new DuplicateKeyException(String.format(
                    Locale.ROOT,
                    "Incompatible set for conversion to function: "
                        + "duplicate key: %s",
                    key
                ));
            }
        }
    }

    public Map<K, V> toMap() {
        return this.toMap(Function.identity(), Function.identity());
    }

    public <M, N> Map<M, N> toMap(
            final Function<? super K, ? extends M> keyMapper,
            final Function<? super V, ? extends N> valueMapper) {

        try {
            return this.stream().collect(
                    Collectors.collectingAndThen(Collectors.toMap(
                            tuple -> keyMapper.apply(tuple.getFirst()),
                            tuple -> valueMapper.apply(tuple.getSecond())
                    ), Collections::unmodifiableMap));
        } catch (final IllegalStateException exception) {
            throw new DuplicateKeyException(exception.getMessage(), exception);
        }
    }
}
