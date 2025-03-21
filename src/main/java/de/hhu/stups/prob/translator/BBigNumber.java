package de.hhu.stups.prob.translator;

import java.math.BigInteger;
import java.util.Objects;

public final class BBigNumber extends BNumber implements BValue {

    private static final long serialVersionUID = 5922463363438789567L;

    private final BigInteger value;

    /* default */ BBigNumber(final BigInteger bigIntegerValue) {
        super();
        this.value = Objects.requireNonNull(bigIntegerValue, "value");
    }

    @Override
    public BigInteger bigIntegerValue() {
        return this.value;
    }
}
