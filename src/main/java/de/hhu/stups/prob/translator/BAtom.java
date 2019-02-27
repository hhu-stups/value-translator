package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BAtom implements BValue {

    private final String value;

    public BAtom(final String text) {
        super();
        this.value = text;
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final BAtom bAtom = (BAtom) other;
        return this.value.equals(bAtom.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public final String toString() {
        return this.value;
    }

    public final String stringValue() {
        return this.value;
    }
}
