package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BString implements BValue {

    private final String value;

    public BString(final String text) {
        super();
        this.value = text;
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BString)) {
            return false;
        }
        final BString bString = (BString) other;
        return this.value.equals(bString.value);
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", this.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    public String stringValue() {
        return this.value;
    }
}
