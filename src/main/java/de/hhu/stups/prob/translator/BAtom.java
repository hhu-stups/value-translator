package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BAtom implements BValue{
    private final String value;

    public BAtom(final String text) {
        this.value = text;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final BAtom bAtom = (BAtom) o;
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
