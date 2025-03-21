package de.hhu.stups.prob.translator;

import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ShortMethodName" })
public abstract class BNumber extends Number implements BValue {

    private static final long serialVersionUID = 5922463363438789565L;

    /* default */ BNumber() {
        super();
    }

    public static BNumber of(final BigInteger bigIntegerValue) {
        Objects.requireNonNull(bigIntegerValue, "value");
        try {
            return of(bigIntegerValue.longValueExact());
        } catch (ArithmeticException ignored) {
            return new BBigNumber(bigIntegerValue);
        }
    }

    public static BNumber of(final int intValue) {
        return of((long) intValue);
    }

    public static BNumber of(final long longValue) {
        return BSmallNumber.create(longValue);
    }

    public static BNumber of(final String stringValue) {
        Objects.requireNonNull(stringValue, "value");
        try {
            return of(Long.parseLong(stringValue));
        } catch (NumberFormatException ignored) {
            return of(new BigInteger(stringValue));
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BNumber)) {
            return false;
        }
        final BNumber bNumber = (BNumber) other;
        return this.bigIntegerValue().equals(bNumber.bigIntegerValue());
    }

    @Override
    public int hashCode() {
        return this.bigIntegerValue().hashCode();
    }

    @Override
    public String toString() {
        return this.bigIntegerValue().toString();
    }

    public abstract BigInteger bigIntegerValue();

    @Override
    public int intValue() {
        return this.bigIntegerValue().intValue();
    }

    public int intValueExact() {
        return this.bigIntegerValue().intValueExact();
    }

    @Override
    public long longValue() {
        return this.bigIntegerValue().longValue();
    }

    public long longValueExact() {
        return this.bigIntegerValue().longValueExact();
    }

    @Override
    public float floatValue() {
        return this.bigIntegerValue().floatValue();
    }

    @Override
    public double doubleValue() {
        return this.bigIntegerValue().doubleValue();
    }
}
