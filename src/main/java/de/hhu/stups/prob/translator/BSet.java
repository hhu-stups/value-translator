package de.hhu.stups.prob.translator;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BSet<T extends BValue> implements BValue{
    final Set<T> values;

    public BSet(final Set<T> values) {
        this.values = values;
    }

    public BSet() {
        this.values = Collections.emptyNavigableSet();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final BSet<?> bSet = (BSet<?>) o;
        return this.values.equals(bSet.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.values);
    }

    @Override
    public String toString() {
        return String.format("{%s}",
                this.values
                        .stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));
    }

    public Set<T> toSet() {
        return Collections.unmodifiableSet(this.values);
    }

    public Stream<T> stream() {
        return this.values.stream();
    }


    @SuppressWarnings("unused")
    public <V extends BValue> BSequence<V> asSequence(final Class<V> valueType) {
        return this.asSequence();
    }

    public <V extends BValue> BSequence<V> asSequence() {
        return new BSequence<>(this.values);
    }

    /**
     * The same as asFunction(), only with additional type hints.
     *
     * @param domainType Class of domain values
     * @param rangeType  Class of range values
     * @return BFuction
     */
    @SuppressWarnings("unused")
    public <A extends BValue, B extends BValue> BFunction<A, B> asFunction(final Class<A> domainType, final Class<B> rangeType) {
        return this.asFunction();
    }

    public <K extends BValue, V extends BValue> BFunction<K, V> asFunction() {
        return new BFunction<>(this.values);
    }

    @SuppressWarnings("unused")
    public <A extends BValue, B extends BValue> BRelation<A, B> asRelation(final Class<A> domainType, final Class<B> rangeType) {
        return this.asRelation();
    }

    public <K extends BValue, V extends BValue> BRelation<K, V> asRelation() {
        return new BRelation<>(this.values);
    }
}
