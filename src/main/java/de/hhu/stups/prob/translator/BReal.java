package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BReal extends Number implements BValue {

    private static final long serialVersionUID = 5922463363438789565L;

    private final double value;

    public BReal(final double doubleValue) {
        super();
        this.value = doubleValue;
    }

    public BReal(final float intValue) {
        this((double) intValue);
    }

    public BReal(final String stringValue) {
        this(Double.parseDouble(stringValue));
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BReal)) {
            return false;
        }
        final BReal bReal = (BReal) other;
        return Double.compare(this.value, bReal.value) == 0;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public int intValue() {
        return (int) this.value;
    }

    @Override
    public long longValue() {
        return (long) this.value;
    }

    @Override
    public float floatValue() {
        return (float) this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }
}
