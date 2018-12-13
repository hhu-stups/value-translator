package de.hhu.stups.prob.translator;

import java.util.Objects;

public class BBoolean implements BValue{
    private final boolean value;

    public BBoolean(final String value) {
        this(Boolean.parseBoolean(value));
    }

    public BBoolean(final boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final BBoolean bBoolean = (BBoolean) o;
        return this.value == bBoolean.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    public Boolean booleanValue() {
        return this.value;
    }
}
