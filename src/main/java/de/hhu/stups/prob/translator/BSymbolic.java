package de.hhu.stups.prob.translator;

import java.util.Objects;

public final class BSymbolic implements BValue {

    private final String value;

    public BSymbolic(final String text) {
        super();
        this.value = Objects.requireNonNull(text, "text");
    }

    @Override
    public boolean equals(final Object other) {
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
