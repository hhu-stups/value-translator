package de.hhu.stups.prob.translator;

public final class BBoolean implements BValue {
    private final boolean value;

    public BBoolean(final String stringValue) {
        this(Boolean.parseBoolean(stringValue));
    }

    public BBoolean(final boolean booleanValue) {
        super();
        this.value = booleanValue;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BBoolean)) {
            return false;
        }
        final BBoolean bBoolean = (BBoolean) other;
        return this.value == bBoolean.value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(this.value);
    }

    public Boolean booleanValue() {
        return this.value;
    }
}
