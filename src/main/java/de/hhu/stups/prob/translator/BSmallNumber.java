package de.hhu.stups.prob.translator;

import java.math.BigInteger;

@SuppressWarnings("PMD.TooManyMethods")
final class BSmallNumber extends BNumber implements BValue {

    private static final BNumber ZERO = new BSmallNumber(0);
    private static final BNumber ONE = new BSmallNumber(1);

    private static final long serialVersionUID = 5922463363438789566L;

    private final long value;

    /* default */ BSmallNumber(final long longValue) {
        super();
        this.value = longValue;
    }

    @SuppressWarnings({ "PMD.OnlyOneReturn", "PMD.AvoidLiteralsInIfCondition" })
    /* default */ static BNumber create(final long value) {
        if (value == 0) {
            return ZERO;
        } else if (value == 1) {
            return ONE;
        } else {
            return new BSmallNumber(value);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public boolean equals(final Object other) {
        if (other instanceof BSmallNumber) {
            return this.value == ((BSmallNumber) other).value;
        } else {
            return super.equals(other);
        }
    }

    @Override
    @SuppressWarnings({ "MagicNumber", "AvoidInlineConditionals",
        "PMD.ShortVariable" })
    public int hashCode() {
        // this is a copy of the BigInteger hashing routine
        // for optimization
        long v = this.value;
        final boolean neg = v < 0;
        if (neg) {
            v = -v;
        }

        final int h = (int) (v >>> 32);
        final int hash = (int) (31 * h + (v & 0xffffffffL));
        return neg ? -hash : hash;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(this.value);
    }

    @Override
    public int intValue() {
        return (int) this.value;
    }

    @Override
    public int intValueExact() {
        return Math.toIntExact(this.value);
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public long longValueExact() {
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
