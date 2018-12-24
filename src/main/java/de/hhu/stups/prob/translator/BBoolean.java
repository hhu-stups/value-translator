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
    @SuppressWarnings("PMD.OnlyOneReturn")
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final BBoolean bBoolean = (BBoolean) other;
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
