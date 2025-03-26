package de.hhu.stups.prob.translator;

import java.util.Objects;

@SuppressWarnings("PMD.ShortMethodName")
public final class BAtom implements BValue {

    private final String value;

    private BAtom(final String text) {
        super();
        this.value = Objects.requireNonNull(text, "text");
    }

    /* default */
    static BAtom of(final String value) {
        return new BAtom(value);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BAtom)) {
            return false;
        }
        final BAtom bAtom = (BAtom) other;
        return this.value.equals(bAtom.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String stringValue() {
        return this.value;
    }
}
