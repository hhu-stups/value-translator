package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BSymbolic implements BValue {

    private final String value;

    public BSymbolic(final String text) {
        super();
        this.value = text;
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BSymbolic)) {
            return false;
        }
        final BSymbolic bSymbolic = (BSymbolic) other;
        return this.value.equals(bSymbolic.value);
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
