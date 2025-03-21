package de.hhu.stups.prob.translator;

public final class BBoolean implements BValue {

    public static final BBoolean TRUE = new BBoolean(true);
    public static final BBoolean FALSE = new BBoolean(false);

    private final boolean value;

    private BBoolean(final boolean booleanValue) {
        this.value = booleanValue;
    }

    @SuppressWarnings("PMD.ShortMethodName")
    public static BBoolean of(final boolean value) {
        if (value) {
            return TRUE;
        } else {
            return FALSE;
        }
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
