package de.hhu.stups.prob.translator;

import java.util.Locale;
import java.util.Objects;

public final class BTuple<T extends BValue, S extends BValue>
    implements BValue {

    private final T first;
    private final S second;

    public BTuple(final T firstValue, final S secondValue) {
        this.first = Objects.requireNonNull(firstValue, "firstValue");
        this.second = Objects.requireNonNull(secondValue, "secondValue");
    }

    public T getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BTuple<?, ?>)) {
            return false;
        }
        final BTuple<?, ?> bTuple = (BTuple<?, ?>) other;
        return this.first.equals(bTuple.first)
                       && this.second.equals(bTuple.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(%s |-> %s)",
            this.first, this.second);
    }
}
