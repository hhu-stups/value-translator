package de.hhu.stups.prob.translator;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.be4.classicalb.core.parser.analysis.AnalysisAdapter;
import de.be4.classicalb.core.parser.node.ABooleanFalseExpression;
import de.be4.classicalb.core.parser.node.ABooleanTrueExpression;
import de.be4.classicalb.core.parser.node.AComprehensionSetExpression;
import de.be4.classicalb.core.parser.node.ACoupleExpression;
import de.be4.classicalb.core.parser.node.AEmptySequenceExpression;
import de.be4.classicalb.core.parser.node.AEmptySetExpression;
import de.be4.classicalb.core.parser.node.AExistsPredicate;
import de.be4.classicalb.core.parser.node.AForallPredicate;
import de.be4.classicalb.core.parser.node.AIdentifierExpression;
import de.be4.classicalb.core.parser.node.AIntegerExpression;
import de.be4.classicalb.core.parser.node.ARealExpression;
import de.be4.classicalb.core.parser.node.ARecEntry;
import de.be4.classicalb.core.parser.node.ARecExpression;
import de.be4.classicalb.core.parser.node.ASequenceExtensionExpression;
import de.be4.classicalb.core.parser.node.ASetExtensionExpression;
import de.be4.classicalb.core.parser.node.AStringExpression;
import de.be4.classicalb.core.parser.node.ASymbolicCompositionExpression;
import de.be4.classicalb.core.parser.node.ASymbolicComprehensionSetExpression;
import de.be4.classicalb.core.parser.node.ASymbolicLambdaExpression;
import de.be4.classicalb.core.parser.node.ASymbolicQuantifiedUnionExpression;
import de.be4.classicalb.core.parser.node.AUnaryMinusExpression;
import de.be4.classicalb.core.parser.node.Node;
import de.be4.classicalb.core.parser.node.PExpression;
import de.be4.classicalb.core.parser.util.PrettyPrinter;
import de.be4.classicalb.core.parser.util.Utils;

@SuppressWarnings({
    "PMD.CouplingBetweenObjects",
    "PMD.ExcessiveImports",
    "PMD.TooManyMethods",
})
public final class TranslatingVisitor<T extends BValue>
        extends AnalysisAdapter {
    private BValue result;
    private boolean inUnaryMinus;

    public TranslatingVisitor() {
        super();
    }

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
            throw new UncheckedException(
                    "Trying to read a missing intermediate result. "
                            + "This might be a missing case in the class "
                            + "TranslatingVisitor.");
        }
        @SuppressWarnings("unchecked")
        final T res = (T) this.result;
        this.result = null;
        return res;
    }

    private void setResult(final BValue bValue) {
        if (this.result != null) {
            throw new UncheckedException("Trying to overwrite an "
                                                    + "intermediate result "
                                                    + "before reading it.");
        }
        this.result = bValue;
    }

    @Override
    public void caseAIntegerExpression(final AIntegerExpression node) {
        final String nodeText = node.getLiteral().getText();
        final String text;
        if (this.inUnaryMinus) {
            text = "-" + nodeText;
        } else {
            text = nodeText;
        }
        this.setResult(BNumber.of(text));
    }

    @Override
    public void caseARealExpression(final ARealExpression node) {
        final String nodeText = node.getLiteral().getText();
        final String text;
        if (this.inUnaryMinus) {
            text = "-" + nodeText;
        } else {
            text = nodeText;
        }
        this.setResult(new BReal(text));
    }

    @Override
    public void caseAUnaryMinusExpression(final AUnaryMinusExpression node) {
        try {
            this.inUnaryMinus = true;
            node.getExpression().apply(this);
        } finally {
            this.inUnaryMinus = false;
        }
    }

    @Override
    public void caseAIdentifierExpression(final AIdentifierExpression node) {
        this.setResult(new BAtom(Utils.getAIdentifierAsString(node)));
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
    public void caseAStringExpression(final AStringExpression node) {
        this.setResult(new BString(node.getContent().getText()));
    }

    @Override
    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public void caseACoupleExpression(final ACoupleExpression node) {
        if (node.getList().size() < 2) {
            throw new UncheckedException(
                    "Unexpected state, couple node containing only one node");
        }

        final Supplier<UncheckedException> supplier
                = () ->
                          new UncheckedException(
                                  "Unexpected state, empty couple node");

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
    public void caseARecEntry(final ARecEntry node) {
        final String identifier = node.getIdentifier().getText();

        final TranslatingVisitor<BValue> visitor =
                new TranslatingVisitor<>();
        node.getValue().apply(visitor);
        final BValue value = visitor.getResult();
        this.setResult(new RecordEntry(identifier, value));
    }

    @Override
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
                                      BNumber.of(index + 1), vtor.getResult());
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

    @Override
    public void defaultCase(final Node node) {
        throw new UncheckedException(
            "Expression type not currently supported by value translator: "
            + node.getClass()
        );
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

    /* default */ static class UncheckedException extends RuntimeException {
        private static final long serialVersionUID = -3036204603081282264L;

        /* default */ UncheckedException(final String message) {
            super(message);
        }
    }
}
