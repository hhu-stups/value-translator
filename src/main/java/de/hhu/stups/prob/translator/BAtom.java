package de.hhu.stups.prob.translator;

import java.util.Objects;

public final class BAtom implements BValue {

    private final String value;

    public BAtom(final String text) {
        super();
        this.value = text;
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
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
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String stringValue() {
        return this.value;
    }
}
