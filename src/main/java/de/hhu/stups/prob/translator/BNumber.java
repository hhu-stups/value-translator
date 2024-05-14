package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BNumber extends Number implements BValue {
    private static final long serialVersionUID = 5922463363438789565L;

    private final Long value;

    public BNumber(final long longValue) {
        super();
        this.value = longValue;
    }

    public BNumber(final int intValue) {
        this((long) intValue);
    }

    public BNumber(final String stringValue) {
        this(Long.parseLong(stringValue));
    }

    @Override
    public String toString() {
        return Integer.toString(this.value.intValue());
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BNumber)) {
            return false;
        }
        final BNumber bNumber = (BNumber) other;
        return this.value.equals(bNumber.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }
}
