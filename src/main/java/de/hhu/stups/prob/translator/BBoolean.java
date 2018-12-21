package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BBoolean implements BValue {
    private final boolean value;

    public BBoolean(final String stringValue) {
        this(Boolean.parseBoolean(stringValue));
    }

    public BBoolean(final boolean booleanValue) {
        super();
        this.value = booleanValue;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BBoolean bBoolean = (BBoolean) o;
        return this.value == bBoolean.value;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    public final Boolean booleanValue() {
        return this.value;
    }
}
