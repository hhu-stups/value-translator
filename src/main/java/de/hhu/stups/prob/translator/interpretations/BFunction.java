package de.hhu.stups.prob.translator.interpretations;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.hhu.stups.prob.translator.BValue;
import de.hhu.stups.prob.translator.exceptions.DuplicateKeyException;

@SuppressWarnings("WeakerAccess")
public class BFunction<K extends BValue, V extends BValue>
        extends BRelation<K, V> {

    public BFunction(final Set<? extends BValue> bValues) {
        super(bValues);
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

    public Map<K, V> toMap() {
        return this.toMap(Function.identity(), Function.identity());
    }
}
