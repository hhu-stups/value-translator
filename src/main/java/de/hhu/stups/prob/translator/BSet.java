package de.hhu.stups.prob.translator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hhu.stups.prob.translator.interpretations.BFunction;
import de.hhu.stups.prob.translator.interpretations.BRelation;
import de.hhu.stups.prob.translator.interpretations.BSequence;

@SuppressWarnings({ "PMD.ShortClassName", "PMD.TooManyMethods" })
public class BSet<T extends BValue> implements BValue {

    private final Set<T> values;

    protected BSet(final Set<? extends T> set) {
        this.values = Collections.unmodifiableSet(
            new HashSet<>(Objects.requireNonNull(set, "set"))
        );
    }

    /* default */
    static <T extends BValue> BSet<T> set(
        final Set<? extends T> values) {

        return new BSet<>(values);
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BSet<?>)) {
            return false;
        }
        final BSet<?> bSet = (BSet<?>) other;
        return this.values.equals(bSet.values);
    }

    @Override
    public final int hashCode() {
        return this.values.hashCode();
    }

    @Override
    public String toString() {
        return this.values.stream()
                   .map(Objects::toString)
                   .collect(Collectors.joining(", ", "{", "}"));
    }

    public Set<T> toSet() {
        // can just return field because we made it immutable in the constructor
        return this.values;
    }

    public Stream<T> stream() {
        return this.values.stream();
    }

    @SuppressWarnings("unused")
    public <V extends BValue> BSequence<V> asSequence(
        final Class<V> valueType) {
        return this.asSequence();
    }

    public <V extends BValue> BSequence<V> asSequence() {
        return BValue.sequenceFromTuples(this.values);
    }

    /**
     * The same as asFunction(), only with additional type hints.
     *
     * @param domainType Class of domain values
     * @param rangeType  Class of range values
     * @param <A>        Type of domain values
     * @param <B>        Type of range values
     * @return BFunction
     */
    @SuppressWarnings("unused")
    public <A extends BValue, B extends BValue> BFunction<A, B> asFunction(
        final Class<A> domainType, final Class<B> rangeType) {
        return this.asFunction();
    }

    public <K extends BValue, V extends BValue> BFunction<K, V> asFunction() {
        return BValue.functionFromTuples(this.values);
    }

    @SuppressWarnings("unused")
    public <A extends BValue, B extends BValue> BRelation<A, B> asRelation(
        final Class<A> domainType, final Class<B> rangeType) {
        return this.asRelation();
    }

    public <K extends BValue, V extends BValue> BRelation<K, V> asRelation() {
        return BValue.relationFromTuples(this.values);
    }
}
