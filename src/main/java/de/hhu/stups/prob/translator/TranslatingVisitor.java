package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.analysis.DepthFirstAdapter;
import de.be4.classicalb.core.parser.node.ABooleanFalseExpression;
import de.be4.classicalb.core.parser.node.ABooleanTrueExpression;
import de.be4.classicalb.core.parser.node.AComprehensionSetExpression;
import de.be4.classicalb.core.parser.node.ACoupleExpression;
import de.be4.classicalb.core.parser.node.AEmptySequenceExpression;
import de.be4.classicalb.core.parser.node.AEmptySetExpression;
import de.be4.classicalb.core.parser.node.AExistsPredicate;
import de.be4.classicalb.core.parser.node.AForallPredicate;
import de.be4.classicalb.core.parser.node.ARecEntry;
import de.be4.classicalb.core.parser.node.ARecExpression;
import de.be4.classicalb.core.parser.node.ASequenceExtensionExpression;
import de.be4.classicalb.core.parser.node.ASetExtensionExpression;
import de.be4.classicalb.core.parser.node.ASymbolicCompositionExpression;
import de.be4.classicalb.core.parser.node.ASymbolicComprehensionSetExpression;
import de.be4.classicalb.core.parser.node.ASymbolicLambdaExpression;
import de.be4.classicalb.core.parser.node.ASymbolicQuantifiedUnionExpression;
import de.be4.classicalb.core.parser.node.AUnaryMinusExpression;
import de.be4.classicalb.core.parser.node.PExpression;
import de.be4.classicalb.core.parser.node.TIdentifierLiteral;
import de.be4.classicalb.core.parser.node.TIntegerLiteral;
import de.be4.classicalb.core.parser.node.TRealLiteral;
import de.be4.classicalb.core.parser.node.TStringLiteral;

import de.be4.classicalb.core.parser.util.PrettyPrinter;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("PMD.TooManyMethods")
public class TranslatingVisitor<T extends BValue> extends DepthFirstAdapter {
    private T result;
    private boolean inUnaryMinus;

    public TranslatingVisitor() {
        super();
    }

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
        return elements.stream().map(expression -> {
            final TranslatingVisitor<BValue> visitor =
                    new TranslatingVisitor<>();
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
    public void caseTRealLiteral(final TRealLiteral node) {
        final String nodeText = node.getText();
        final String text;
        if (this.inUnaryMinus) {
            text = "-" + nodeText;
        } else {
            text = nodeText;
        }
        this.setResult(new BReal(text));
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

    @Override
    public void caseAComprehensionSetExpression(
            final AComprehensionSetExpression node) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.apply(prettyPrinter);
        this.setResult(new BSymbolic(prettyPrinter.getPrettyPrint()));
    }

    @Override
    public void caseASymbolicComprehensionSetExpression(
            final ASymbolicComprehensionSetExpression node) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.apply(prettyPrinter);
        this.setResult(new BSymbolic(prettyPrinter.getPrettyPrint()));
    }

    @Override
    public void caseASymbolicCompositionExpression(
            final ASymbolicCompositionExpression node) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.apply(prettyPrinter);
        this.setResult(new BSymbolic(prettyPrinter.getPrettyPrint()));
    }

    @Override
    public void caseASymbolicLambdaExpression(
            final ASymbolicLambdaExpression node) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.apply(prettyPrinter);
        this.setResult(new BSymbolic(prettyPrinter.getPrettyPrint()));
    }

    @Override
    public void caseASymbolicQuantifiedUnionExpression(
            final ASymbolicQuantifiedUnionExpression node) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.apply(prettyPrinter);
        this.setResult(new BSymbolic(prettyPrinter.getPrettyPrint()));
    }

    @Override
    public void caseAExistsPredicate(
            final AExistsPredicate node) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.apply(prettyPrinter);
        this.setResult(new BSymbolic(prettyPrinter.getPrettyPrint()));
    }

    @Override
    public void caseAForallPredicate(
            final AForallPredicate node) {
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        node.apply(prettyPrinter);
        this.setResult(new BSymbolic(prettyPrinter.getPrettyPrint()));
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



        this.inACoupleExpression(node);
        final BValue bValue
                = node.getList()
                          .stream()
                          .map(expression -> {
                              final TranslatingVisitor<BValue> visitor =
                                      new TranslatingVisitor<>();
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
        this.setResult(
                node.getEntries().stream().map(recEntry -> {
                    final TranslatingVisitor<BValue> visitor =
                            new TranslatingVisitor<>();
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

        final TranslatingVisitor<BValue> visitor =
                new TranslatingVisitor<>();
        node.getValue().apply(visitor);
        final BValue value = visitor.getResult();
        this.setResult(
                new RecordEntry(depthFirstAdapter.getIdentifier(), value));
    }

    @Override
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void caseASequenceExtensionExpression(
            final ASequenceExtensionExpression node) {

        final List<PExpression> expressions = node.getExpression();
        final Set<BTuple<BNumber, ?>> values
                = IntStream
                          .range(0, expressions.size())
                          .mapToObj(index -> {
                              final TranslatingVisitor<BValue> vtor =
                                      new TranslatingVisitor<>();
                              expressions.get(index).apply(vtor);

                              return new BTuple<>(
                                      new BNumber(index + 1), vtor.getResult());
                          }).collect(Collectors.toSet());

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
        private static final long serialVersionUID = -1476883967744949095L;

        /* default */ UnexpectedTypeException(
                final String message,
                final ClassCastException exception) {
            super(message, exception);
        }
    }

    /* default */ static class IllegalStateException extends RuntimeException {
        private static final long serialVersionUID = -3036204603081282264L;

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
