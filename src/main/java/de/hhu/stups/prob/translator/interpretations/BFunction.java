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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("WeakerAccess")
public class BFunction<K extends BValue, V extends BValue>
        extends BRelation<K, V> {

    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    public BFunction(final Set<? extends BValue> bValues) {
        super(bValues);
        final Set<BValue> keys = new HashSet<>();
        for (final BValue value : bValues) {
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
