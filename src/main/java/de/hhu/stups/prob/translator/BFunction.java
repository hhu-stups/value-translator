package de.hhu.stups.prob.translator;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class BFunction<K extends BValue, V extends BValue> extends BRelation<K, V>{

    public BFunction(final Set<? extends BValue> bValues) {
        super(bValues);
    }

    // TODO: improve use of generics with extends and super
    public <M, N> Map<M, N> toMap(
            final Function<? super K, ? extends M> keyMapper,
            final Function<? super V, ? extends N> valueMapper) {

        return this.values.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toMap(
                                o -> keyMapper.apply(o.first()),
                                o -> valueMapper.apply(o.second())),
                        Collections::unmodifiableMap));
    }

    public Map<K, V> toMap() {
        return this.toMap(Function.identity(), Function.identity());
    }
}
