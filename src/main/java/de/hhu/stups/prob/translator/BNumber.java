package de.hhu.stups.prob.translator;

import java.util.Objects;

// TODO: support bignumbers?
public class BNumber extends Number implements BValue {
    private final Long value;

    public BNumber(final long longValue) {
        super();
        this.value = longValue;
    }

    public BNumber(final String stringValue) {
        this(Integer.parseInt(stringValue));
    }

    @Override
    public String toString() {
        return String.format("BNumber(%s)", this.value.toString());
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final BNumber bNumber = (BNumber) other;
        return this.value.equals(bNumber.value);
    }

    @Override
    public int hashCode() {
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
        return this.value.floatValue();
    }

    @Override
    public double doubleValue() {
        return this.value.doubleValue();
    }
}
