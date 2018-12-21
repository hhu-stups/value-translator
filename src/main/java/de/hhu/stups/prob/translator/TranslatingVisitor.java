package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.analysis.DepthFirstAdapter;
import de.be4.classicalb.core.parser.node.ABooleanFalseExpression;
import de.be4.classicalb.core.parser.node.ABooleanTrueExpression;
import de.be4.classicalb.core.parser.node.ACoupleExpression;
import de.be4.classicalb.core.parser.node.AEmptySequenceExpression;
import de.be4.classicalb.core.parser.node.AEmptySetExpression;
import de.be4.classicalb.core.parser.node.ARecEntry;
import de.be4.classicalb.core.parser.node.ARecExpression;
import de.be4.classicalb.core.parser.node.ASequenceExtensionExpression;
import de.be4.classicalb.core.parser.node.ASetExtensionExpression;
import de.be4.classicalb.core.parser.node.AUnaryMinusExpression;
import de.be4.classicalb.core.parser.node.PExpression;
import de.be4.classicalb.core.parser.node.PRecEntry;
import de.be4.classicalb.core.parser.node.TIdentifierLiteral;
import de.be4.classicalb.core.parser.node.TIntegerLiteral;
import de.be4.classicalb.core.parser.node.TStringLiteral;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TranslatingVisitor<T extends BValue> extends DepthFirstAdapter {
    private T result;
    private boolean inUnaryMinus;

    @SuppressWarnings("unchecked")
    private static <E extends BValue> E cast(final Object result) {
        try {
            return (E) result;
        } catch (final ClassCastException exception) {
            throw new UnexpectedTypeException(exception.getMessage());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public T getResult() {
        if (this.result == null) {
            throw new TranslatingVisitor.IllegalStateException(
                    "Trying to read a missing intermediate result. "
                            + "This might be a missing case in the class "
                            + "TranslatingVisitor.");
        }
        final T res = this.result;
        this.result = null;
        return res;
    }

    private void setResult(final BValue bValue) {
        if (this.result != null) {
            throw new IllegalStateException("Trying to overwrite an "
                                                    + "intermediate result "
                                                    + "before reading it.");
        }
        this.result = cast(bValue);
    }

    @Override
    public void caseTIntegerLiteral(final TIntegerLiteral node) {
        java.lang.String text = node.getText();
        if (this.inUnaryMinus) {
            text = "-" + text;
        }
        this.setResult(BNumber.build(text));
    }

    @Override
    public void inAUnaryMinusExpression(final AUnaryMinusExpression node) {
        this.inUnaryMinus = true;
    }

    @Override
    public void outAUnaryMinusExpression(final AUnaryMinusExpression node) {
        this.inUnaryMinus = false;
    }

    @Override
    public void caseTIdentifierLiteral(final TIdentifierLiteral node) {
        this.setResult(new BAtom(node.getText()));
    }


    @Override
    public void caseAEmptySetExpression(final AEmptySetExpression node) {
        this.setResult(new BSet<>());
    }

    @Override
    public void caseASetExtensionExpression(
            final ASetExtensionExpression node) {
        final java.util.Set<BValue> elements
                = this.listToSet(node.getExpressions());
        this.setResult(new BSet<>(elements));
    }

    @Override
    public void caseTStringLiteral(final TStringLiteral node) {
        this.setResult(new BString(node.getText()));
    }

    //

    @Override
    public void caseACoupleExpression(final ACoupleExpression node) {
        if (node.getList().size() != 2) {
            // TODO: custom exception
            throw new AssertionError();
        }

        final List<BValue> results = new ArrayList<>();

        this.inACoupleExpression(node);
        final List<PExpression> copy = new ArrayList<>(node.getList());
        for (final PExpression expression : copy) {
            expression.apply(this);
            results.add(this.getResult());
        }
        this.outACoupleExpression(node);

        this.setResult(new BTuple<>(results.get(0), results.get(1)));
    }

    //
    @Override
    public void caseARecExpression(final ARecExpression node) {
        final Map<String, BValue> s = BRecord.newStorage();
        // TODO or make the record immutable after filling it
        for (final PRecEntry e : node.getEntries()) {
            e.apply(this);
            final RecordEntry entry = (RecordEntry) this.getResult();
            s.put(entry.key, entry.value);
        }
        this.setResult(new BRecord(s));
    }


    @Override
    public void caseARecEntry(final ARecEntry node) {
        final String[] identifier = {null};
        node.getIdentifier().apply(new DepthFirstAdapter() {
            @Override
            public void caseTIdentifierLiteral(final TIdentifierLiteral node) {
                identifier[0] = node.getText();
            }
        });

        node.getValue().apply(this);
        final BValue value = this.getResult();
        this.setResult(new RecordEntry(identifier[0], value));
    }

    @Override
    public void caseASequenceExtensionExpression(
            final ASequenceExtensionExpression node) {
        final Set<BTuple<BNumber, ?>> values = new HashSet<>();

        int counter = 1;
        for (final PExpression e : node.getExpression()) {
            e.apply(this);

            final BTuple<BNumber, ?> tuple
                    = new BTuple<>(new BNumber(counter++), this.getResult());
            values.add(tuple);
        }
        this.setResult(new BSet<>(values));
    }

    @Override
    public void caseAEmptySequenceExpression(
            final AEmptySequenceExpression node) {
        this.setResult(new BSet<>());
    }

    @Override
    public void caseABooleanTrueExpression(final ABooleanTrueExpression node) {
        this.setResult(new BBoolean(true));
    }

    @Override
    public void caseABooleanFalseExpression(
            final ABooleanFalseExpression node) {
        this.setResult(new BBoolean(false));
    }

    private Set<BValue> listToSet(final LinkedList<PExpression> elements) {
        return elements.stream().map(p -> {
            final TranslatingVisitor<BValue> visitor
                    = new TranslatingVisitor<>();
            p.apply(visitor);
            return visitor.getResult();
        }).collect(Collectors.toSet());
    }

    private static final class RecordEntry implements BValue {
        private final BValue value;
        private final String key;

        RecordEntry(final String stringKey, final BValue bValue) {
            super();
            this.key = stringKey;
            this.value = bValue;
        }
    }

    static class UnexpectedTypeException extends RuntimeException {
        UnexpectedTypeException(final String message) {
            super(message);
        }
    }

    static class IllegalStateException extends RuntimeException {
        IllegalStateException(final String message) {
            super(message);
        }
    }
}
