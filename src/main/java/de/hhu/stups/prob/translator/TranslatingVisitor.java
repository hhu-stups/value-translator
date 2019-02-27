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
import de.be4.classicalb.core.parser.node.TIdentifierLiteral;
import de.be4.classicalb.core.parser.node.TIntegerLiteral;
import de.be4.classicalb.core.parser.node.TStringLiteral;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("PMD.TooManyMethods")
public class TranslatingVisitor<T extends BValue> extends DepthFirstAdapter {
    private T result;
    private boolean inUnaryMinus;

    @SuppressWarnings("unchecked")
    private static <E extends BValue> E cast(final Object result) {
        try {
            return (E) result;
        } catch (final ClassCastException exception) {
            throw new UnexpectedTypeException(
                    exception.getMessage(), exception);
        }
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private static Set<BValue> listToSet(final List<PExpression> elements) {
        final TranslatingVisitor<BValue> visitor = new TranslatingVisitor<>();
        return elements.stream().map(expression -> {
            expression.apply(visitor);
            return visitor.getResult();
        }).collect(Collectors.toSet());
    }

    @SuppressWarnings({"WeakerAccess", "PMD.NullAssignment"})
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
    @SuppressWarnings("PMD.AvoidFinalLocalVariable")
    public void caseTIntegerLiteral(final TIntegerLiteral node) {
        final String nodeText = node.getText();
        final String text;
        if (this.inUnaryMinus) {
            text = "-" + nodeText;
        } else {
            text = nodeText;
        }
        this.setResult(new BNumber(text));
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
        final Set<BValue> elements
                = listToSet(node.getExpressions());
        this.setResult(new BSet<>(elements));
    }

    //

    @Override
    public void caseTStringLiteral(final TStringLiteral node) {
        this.setResult(new BString(node.getText()));
    }

    @Override
    @SuppressWarnings({"PMD.AvoidLiteralsInIfCondition",
            "PMD.DataflowAnomalyAnalysis"})
    public void caseACoupleExpression(final ACoupleExpression node) {
        if (node.getList().size() < 2) {
            throw new TranslatingVisitor.IllegalStateException(
                    "Unexpected state, couple node containing only one node");
        }

        final Supplier<IllegalStateException> supplier
                = () ->
                          new IllegalStateException(
                                  "Unexpected state, empty couple node");

        final TranslatingVisitor<BValue> visitor = new TranslatingVisitor<>();

        this.inACoupleExpression(node);
        final BValue bValue
                = node.getList()
                          .stream()
                          .map(expression -> {
                              expression.apply(visitor);
                              return visitor.getResult();
                          })
                          .reduce(BTuple::new)
                          .orElseThrow(supplier);
        this.outACoupleExpression(node);
        this.setResult(bValue);
    }

    //
    @Override
    public void caseARecExpression(final ARecExpression node) {
        final TranslatingVisitor<BValue> visitor = new TranslatingVisitor<>();
        this.setResult(
                node.getEntries().stream().map(recEntry -> {
                    recEntry.apply(visitor);
                    return (RecordEntry) visitor.getResult();
                }).collect(
                        Collectors.collectingAndThen(
                                Collectors.toMap(RecordEntry::getKey,
                                        RecordEntry::getValue),
                                BRecord::new)));
    }

    @Override
    @SuppressWarnings("PMD.AccessorMethodGeneration")
    public void caseARecEntry(final ARecEntry node) {
        final IdentifierDepthFirstAdapter depthFirstAdapter
                = new IdentifierDepthFirstAdapter();
        node.getIdentifier().apply(depthFirstAdapter);

        node.getValue().apply(this);
        final BValue value = this.getResult();
        this.setResult(
                new RecordEntry(depthFirstAdapter.getIdentifier(), value));
    }

    @Override
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void caseASequenceExtensionExpression(
            final ASequenceExtensionExpression node) {
        final Set<BTuple<BNumber, ?>> values = new HashSet<>();

        final List<PExpression> expressions = node.getExpression();
        for (int i = 0; i < expressions.size(); i++) {
            final PExpression expression = expressions.get(i);
            expression.apply(this);

            final BTuple<BNumber, ?> tuple
                    = new BTuple<>(new BNumber(i + 1), this.getResult());
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
    public void caseABooleanTrueExpression(
            final ABooleanTrueExpression node) {
        this.setResult(new BBoolean(true));
    }

    @Override
    public void caseABooleanFalseExpression(
            final ABooleanFalseExpression node) {
        this.setResult(new BBoolean(false));
    }

    private static final class RecordEntry implements BValue {
        private final BValue value;
        private final String key;

        /* default */ RecordEntry(final String stringKey, final BValue bValue) {
            super();
            this.key = stringKey;
            this.value = bValue;
        }

        private BValue getValue() {
            return this.value;
        }

        private String getKey() {
            return this.key;
        }
    }

    /* default */ static class UnexpectedTypeException
            extends RuntimeException {
        /* default */ UnexpectedTypeException(
                final String message,
                final ClassCastException exception) {
            super(message, exception);
        }
    }

    /* default */ static class IllegalStateException extends RuntimeException {
        /* default */ IllegalStateException(final String message) {
            super(message);
        }
    }

    /* default */ private static class IdentifierDepthFirstAdapter
            extends DepthFirstAdapter {
        private String identifier;

        @Override
        public void caseTIdentifierLiteral(final TIdentifierLiteral node) {
            this.identifier = node.getText();
        }

        public String getIdentifier() {
            return this.identifier;
        }
    }
}
