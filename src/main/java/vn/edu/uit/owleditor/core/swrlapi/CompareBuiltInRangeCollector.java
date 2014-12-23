package vn.edu.uit.owleditor.core.swrlapi;

import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary;
import org.swrlapi.builtins.arguments.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CompareBuiltInRangeCollector implements SWRLBuiltInArgumentVisitorEx<OWLLiteral> {

    private static final Set<SWRLBuiltInsVocabulary> allowedSWRLVocab =
            new HashSet<SWRLBuiltInsVocabulary>() {
                private static final long serialVersionUID = 1L;

                {
                    add(SWRLBuiltInsVocabulary.GREATER_THAN);
                    add(SWRLBuiltInsVocabulary.GREATER_THAN_OR_EQUAL);
                    add(SWRLBuiltInsVocabulary.EQUAL);
                    add(SWRLBuiltInsVocabulary.NOT_EQUAL);
                    add(SWRLBuiltInsVocabulary.LESS_THAN);
                    add(SWRLBuiltInsVocabulary.LESS_THAN_OR_EQUAL);
                }
            };
    private final SWRLBuiltInsVocabulary builtInVocab;
    private final Map<SWRLBuiltInsVocabulary, OWLLiteral> pop;

    public CompareBuiltInRangeCollector(SWRLBuiltInsVocabulary vocab) {
        this.builtInVocab = vocab;
        pop = new HashMap<SWRLBuiltInsVocabulary, OWLLiteral>();
    }

    public Map<SWRLBuiltInsVocabulary, OWLLiteral> getLiterals() {
        return pop;
    }

    @Override
    public OWLLiteral visit(SWRLClassBuiltInArgument argument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OWLLiteral visit(SWRLNamedIndividualBuiltInArgument argument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OWLLiteral visit(SWRLObjectPropertyBuiltInArgument argument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OWLLiteral visit(SWRLDataPropertyBuiltInArgument argument) {
        return null;
    }

    @Override
    public OWLLiteral visit(SWRLAnnotationPropertyBuiltInArgument argument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OWLLiteral visit(SWRLDatatypeBuiltInArgument argument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OWLLiteral visit(SWRLLiteralBuiltInArgument argument) {
        System.out.println(argument);
        return argument.getLiteral();
    }

    @Override
    public OWLLiteral visit(SWRLVariableBuiltInArgument argument) {
        System.out.println(argument.getIRI());
        return null;
    }

    @Override
    public OWLLiteral visit(SWRLMultiValueVariableBuiltInArgument argument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OWLLiteral visit(SQWRLCollectionVariableBuiltInArgument argument) {
        // TODO Auto-generated method stub
        return null;
    }
}
