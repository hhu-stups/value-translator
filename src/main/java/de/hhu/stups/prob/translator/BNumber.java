package de.hhu.stups.prob.translator;

import java.util.Objects;

// TODO: support bignumbers?
public class BNumber extends Number implements BValue {
    private final Long value;

    public BNumber(final long longValue) {
        this.value = longValue;
    }

    static BNumber build(final String text) {
        return new BNumber(Integer.parseInt(text));

    }

    @Override
    public String toString() {
        return String.format("BNumber(%s)", this.value.toString());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BNumber bNumber = (BNumber) o;
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
