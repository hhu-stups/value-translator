package de.hhu.stups.prob.translator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("PMD.ShortMethodName")
public final class BRecord implements BValue {

    private final Map<String, BValue> values;

    private BRecord(final Map<String, ? extends BValue> valueMap) {
        super();
        this.values = Collections.unmodifiableMap(new HashMap<>(
            Objects.requireNonNull(valueMap, "values")
        ));
    }

    /* default */
    static BRecord of(final Map<String, ? extends BValue> values) {
        return new BRecord(values);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BRecord)) {
            return false;
        }
        final BRecord bRecord = (BRecord) other;
        return Objects.equals(this.values, bRecord.values);
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(", ", "rec(", ")");
        this.values.forEach((key, value) -> joiner.add(key + ":" + value));
        return joiner.toString();
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Map<String, BValue> toMap() {
        // can just return field because we made it immutable in the constructor
        return this.values;
    }
}
