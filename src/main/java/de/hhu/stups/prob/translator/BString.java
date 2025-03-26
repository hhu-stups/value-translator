package de.hhu.stups.prob.translator;

import java.util.Objects;

import de.be4.classicalb.core.parser.util.Utils;

@SuppressWarnings("PMD.ShortMethodName")
public final class BString implements BValue {

    private final String value;

    private BString(final String text) {
        super();
        this.value = Objects.requireNonNull(text, "text");
    }

    /* default */
    static BString of(final String value) {
        return new BString(value);
    }

    @Override
    public boolean equals(final Object other) {
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
        return "\"" + Utils.escapeStringContents(this.value) + "\"";
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    public String stringValue() {
        return this.value;
    }
}
