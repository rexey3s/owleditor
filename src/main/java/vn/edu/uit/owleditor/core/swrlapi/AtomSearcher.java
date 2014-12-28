package vn.edu.uit.owleditor.core.swrlapi;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary;
import org.swrlapi.builtins.arguments.SWRLLiteralBuiltInArgument;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRule;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/25/14.
 */
public class AtomSearcher {
    private static final IRI[] filteredBuiltIn = {
            SWRLBuiltInsVocabulary.GREATER_THAN.getIRI(),
            SWRLBuiltInsVocabulary.GREATER_THAN_OR_EQUAL.getIRI(),
            SWRLBuiltInsVocabulary.EQUAL.getIRI(),
            SWRLBuiltInsVocabulary.NOT_EQUAL.getIRI(),
            SWRLBuiltInsVocabulary.LESS_THAN.getIRI(),
            SWRLBuiltInsVocabulary.LESS_THAN_OR_EQUAL.getIRI()};
    private static final List<IRI> filteredBuiltinList = Arrays.asList(filteredBuiltIn);
    private final Multimap<OWLObjectProperty, Multimap<Set<OWLClass>, Set<SWRLAtom>>> objectPropertiesSuggestion = LinkedListMultimap.create();
    private final Multimap<OWLDataProperty, Multimap<Object, Set<SWRLAtom>>> dataPropertySuggestions = LinkedListMultimap.create();
    private final SWRLAPIOWLOntology ontology;
    private final Set<SWRLAPIRule> rules = new HashSet<>();

    public AtomSearcher(SWRLAPIOWLOntology swrlapiowlOntology) {
        ontology = swrlapiowlOntology;
        ontology.getSWRLAPIRules().stream()
                .filter(rule -> !rule.isSQWRLQuery())
                .forEach(rules::add);

    }

    public static Multimap<OWLObjectProperty, Multimap<Set<OWLClass>, Set<SWRLAtom>>> getObjectPropertySuggestion(OWLClass owlClass, SWRLAPIOWLOntology ontology) {
        return new AtomSearcher(ontology).getObjectPropertySuggestionByClass(owlClass);
    }

    public static Multimap<OWLDataProperty, Multimap<Object, Set<SWRLAtom>>> getDataProperySuggestion(OWLClass owlClass, SWRLAPIOWLOntology ontology) {
        return new AtomSearcher(ontology).getDataSuggestionByClass(owlClass);
    }

    private static Set<OWLClass> collectObjectPropertyEndpoint(SWRLRule rule, SWRLObjectPropertyAtom propertyAtom) {
        final Set<OWLClass> classesSuggestion = new HashSet<>();
        rule.accept(new SWRLObjectVisitorAdapter() {
            @Override
            public void visit(SWRLRule rule) {
                rule.getBody().forEach(atom -> atom.accept(this));

            }

            @Override
            public void visit(SWRLClassAtom classAtom) {
                if (propertyAtom.getSecondArgument().equals(classAtom.getArgument()) && !classAtom.getPredicate().isAnonymous()) {
                    classesSuggestion.add(classAtom.getPredicate().asOWLClass());

                }
            }
        });
        return classesSuggestion;
    }

    private static Map<IRI, OWLLiteral> collectLiteralEndpoint(SWRLAPIRule rule, SWRLVariable x) {
        final Map<IRI, OWLLiteral> literalsSuggestion = new HashMap<>();

        rule.accept(new SWRLObjectVisitorAdapter() {
            @Override
            public void visit(SWRLRule rule1) {
                rule1.getBody().forEach(atom1 -> atom1.accept(this));
            }
            @Override
            public void visit(SWRLBuiltInAtom builtInAtom) {
                if (filteredBuiltinList.contains(builtInAtom.getPredicate())) {
                    List<SWRLDArgument> args = builtInAtom.getArguments();
                    if (builtInAtom.isCoreBuiltIn() && args.contains(x)) {
                        args.remove(x);
                        builtInAtom.getArguments().forEach(arg -> {
                            if (arg instanceof SWRLLiteralBuiltInArgument) {
                                literalsSuggestion.put(builtInAtom.getPredicate(), ((SWRLLiteralBuiltInArgument) arg).getLiteral());
                            }
                        });
                    }
                }
            }

        });
        return literalsSuggestion;
    }

    private Multimap<OWLObjectProperty, Multimap<Set<OWLClass>, Set<SWRLAtom>>> getObjectPropertySuggestionByClass(OWLClass clzz) {

        objectPropertiesSuggestion.clear();
        Set<SWRLRule> affectedRules = new HashSet<>();
        for (SWRLRule rule : rules) {
            affectedRules.addAll(rule.getBody()
                    .stream().filter(atom -> atom.containsEntityInSignature(clzz))
                    .map(atom -> rule).collect(Collectors.toList()));
        }
        /* To be more efficient */
        affectedRules.forEach(rule -> rule.getBody().forEach(atom -> atom.accept(new SWRLObjectVisitorAdapter() {
            @Override
            public void visit(SWRLObjectPropertyAtom propertyAtom) {
                final Multimap<Set<OWLClass>, Set<SWRLAtom>> consequence = LinkedListMultimap.create();
                consequence.put(collectObjectPropertyEndpoint(rule, propertyAtom), rule.getHead());
                objectPropertiesSuggestion.put(propertyAtom.getPredicate().asOWLObjectProperty(), consequence);
            }
        })));

        return objectPropertiesSuggestion;
    }

    private Multimap<OWLDataProperty, Multimap<Object, Set<SWRLAtom>>> getDataSuggestionByClass(OWLClass clzz) {

        dataPropertySuggestions.clear();
        Set<SWRLAPIRule> affectedRules = new HashSet<>();

        for (SWRLAPIRule rule : rules) {
            affectedRules.addAll(rule.getBody().stream().filter(atom -> atom.containsEntityInSignature(clzz)).map(atom -> rule).collect(Collectors.toList()));
        }

        affectedRules.forEach(rule -> rule.getBody().forEach(atom -> atom.accept(new SWRLObjectVisitorAdapter() {
            @Override
            public void visit(SWRLDataPropertyAtom propertyAtom) {
                final Multimap<Object, Set<SWRLAtom>> consequence = LinkedListMultimap.create();
                propertyAtom.getSecondArgument().accept(new SWRLObjectVisitorAdapter() {
                    @Override
                    public void visit(SWRLLiteralArgument node) {
                        consequence.put(node.getLiteral(), rule.getHead());
                    }

                    @Override
                    public void visit(SWRLVariable variable) {
                        Map<IRI, OWLLiteral> variableDependencies = collectLiteralEndpoint(rule, variable);
                        if (!variableDependencies.isEmpty())
                            consequence.put(variableDependencies, rule.getHead());

                    }
                });
                dataPropertySuggestions.put(propertyAtom.getPredicate().asOWLDataProperty(), consequence);
            }
        })));
        return dataPropertySuggestions;
    }

}
