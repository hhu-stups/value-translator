package de.hhu.stups.prob.translator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

// Is there a type safe method to provide access to the fields using
// the specific field type?
@SuppressWarnings("WeakerAccess")
public final class BRecord implements BValue {
    private final Map<String, BValue> values;

    public BRecord(final Map<String, BValue> valueMap) {
        super();
        this.values = new HashMap<>(
            Objects.requireNonNull(valueMap, "valueMap"));
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

    public Map<String, BValue> toMap() {
        return Collections.unmodifiableMap(this.values);
    }
}
