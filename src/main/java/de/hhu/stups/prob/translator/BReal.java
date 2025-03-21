package de.hhu.stups.prob.translator;

@SuppressWarnings("PMD.ShortMethodName")
public final class BReal extends Number implements BValue {

    private static final long serialVersionUID = 5922463363438789565L;

    private final double value;

    private BReal(final double doubleValue) {
        super();
        this.value = doubleValue;
    }

    public static BReal of(final String stringValue) {
        return of(Double.parseDouble(stringValue));
    }

    public static BReal of(final double doubleValue) {
        if (!Double.isFinite(doubleValue)) {
            throw new IllegalArgumentException("value must be finite");
        }
        return new BReal(doubleValue);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(final Object other) {
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
    public int hashCode() {
        return Double.hashCode(this.value);
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
