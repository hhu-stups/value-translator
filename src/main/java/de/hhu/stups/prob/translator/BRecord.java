package de.hhu.stups.prob.translator;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

// Is there a type safe method to provide access to the fields using
// the specific field type?
@SuppressWarnings("WeakerAccess")
public class BRecord implements BValue {
    private final Map<String, BValue> values;

    public BRecord(final Map<String, BValue> valueMap) {
        super();
        this.values = valueMap;
    }

    public static Map<String, BValue> newStorage() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BRecord bRecord = (BRecord) o;
        return Objects.equals(this.values, bRecord.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.values);
    }

    public Map<String, BValue> toMap() {
        return Collections.unmodifiableMap(this.values);
    }
}
