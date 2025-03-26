package de.hhu.stups.prob.translator;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hhu.stups.prob.translator.interpretations.BFunction;
import de.hhu.stups.prob.translator.interpretations.BRelation;
import de.hhu.stups.prob.translator.interpretations.BSequence;

@SuppressWarnings({ "PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals" })
public interface BValue {

    static BBoolean bool(final boolean value) {
        return BBoolean.of(value);
    }

    static BBoolean bool(final String value) {
        return BBoolean.of(value);
    }

    static BNumber number(final long value) {
        return BNumber.of(value);
    }

    static BNumber number(final BigInteger value) {
        return BNumber.of(value);
    }

    static BNumber number(final String value) {
        return BNumber.of(value);
    }

    static BReal real(final double value) {
        return BReal.of(value);
    }

    static BReal real(final String value) {
        return BReal.of(value);
    }

    static BString string(final String value) {
        return BString.of(value);
    }

    static BAtom atom(final String value) {
        return BAtom.of(value);
    }

    static BSymbolic symbolic(final String value) {
        return BSymbolic.of(value);
    }

    static <T extends BValue, S extends BValue> BTuple<T, S> tuple(
        final T left, final S right) {

        return BTuple.of(left, right);
    }

    static BRecord record(final Map<String, ? extends BValue> values) {
        return BRecord.of(values);
    }

    static <T extends BValue> BSet<T> set() {
        return set(Collections.emptySet());
    }

    static <T extends BValue> BSet<T> set(final Set<? extends T> values) {
        return BSet.set(values);
    }

    @SuppressWarnings("deprecation")
    static <K extends BValue, V extends BValue> BRelation<K, V> relation(
        final Map<? extends K, Set<? extends V>> values) {

        return BRelation.relation(values);
    }

    @SuppressWarnings("deprecation")
    static <K extends BValue, V extends BValue> BRelation<K, V>
    relationFromTuples(final Set<? extends BValue> tuples) {
        return BRelation.relationFromTuples(tuples);
    }

    @SuppressWarnings("deprecation")
    static <K extends BValue, V extends BValue> BFunction<K, V>
    function(final Map<? extends K, ? extends V> values) {
        return BFunction.function(values);
    }

    @SuppressWarnings("deprecation")
    static <K extends BValue, V extends BValue> BFunction<K, V>
    functionFromTuples(final Set<? extends BValue> tuples) {
        return BFunction.functionFromTuples(tuples);
    }

    static <V extends BValue> BSequence<V> sequence() {
        return sequence(Collections.emptyList());
    }

    @SuppressWarnings("deprecation")
    static <V extends BValue> BSequence<V> sequence(
        final List<? extends V> values) {

        return BSequence.sequence(values);
    }

    @SuppressWarnings("deprecation")
    static <V extends BValue> BSequence<V> sequenceFromTuples(
        final Set<? extends BValue> tuples) {

        return BSequence.sequenceFromTuples(tuples);
    }
}
