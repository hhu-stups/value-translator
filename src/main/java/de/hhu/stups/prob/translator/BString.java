package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BString implements BValue {

    private final String value;

    public BString(final String text) {
        super();
        this.value = text;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BString bString = (BString) o;
        return this.value.equals(bString.value);
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    public String stringValue() {
        return this.value;
    }
}
