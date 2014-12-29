package vn.edu.uit.owleditor.core.swrlapi;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary;
import org.swrlapi.builtins.arguments.SWRLLiteralBuiltInArgument;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRule;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/29/14.
 */
public class DataPropertyDependenciesSearcher {
    private static final List<IRI> filteredBuiltinList = Arrays.asList(
            SWRLBuiltInsVocabulary.GREATER_THAN.getIRI(),
            SWRLBuiltInsVocabulary.GREATER_THAN_OR_EQUAL.getIRI(),
            SWRLBuiltInsVocabulary.EQUAL.getIRI(),
            SWRLBuiltInsVocabulary.NOT_EQUAL.getIRI(),
            SWRLBuiltInsVocabulary.LESS_THAN.getIRI(),
            SWRLBuiltInsVocabulary.LESS_THAN_OR_EQUAL.getIRI());
    private static OWLObjectRenderer renderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
    private final JsonObject dataSet = new JsonObject();
    private final Multimap<OWLDataProperty, Multimap<Object, Set<SWRLAtom>>> dataPropertySuggestions = LinkedListMultimap.create();
    private final Set<SWRLAPIRule> rules = new HashSet<>();
    private JsonArray nodes = new JsonArray();
    private JsonArray edges = new JsonArray();

    public DataPropertyDependenciesSearcher(SWRLAPIOWLOntology swrlapiowlOntology) {
        swrlapiowlOntology.getSWRLAPIRules().stream()
                .filter(rule -> !rule.isSQWRLQuery())
                .forEach(rules::add);
        dataSet.add("nodes", nodes);
        dataSet.add("edges", edges);
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

    private JsonObject buildOWLObjectNode(OWLObject object) {
        final JsonObject jsonEntity = new JsonObject();
        final String shortForm = renderer.render(object);
        jsonEntity.addProperty("id", shortForm);
        jsonEntity.addProperty("label", shortForm);
//        jsonEntity.addProperty("type", object.getEntityType().toString());
        return jsonEntity;
    }

    private JsonObject buildDataNode(String id) {
        final JsonObject jsonEntity = new JsonObject();

        jsonEntity.addProperty("id", id);
        jsonEntity.addProperty("label", id);
//        jsonEntity.addProperty("type", object.getEntityType().toString());
        return jsonEntity;
    }

    private JsonObject buildDataEdge(String start, String end, String edgeLabel) {

        final JsonObject edge = new JsonObject();
        edge.addProperty("start", start);
        edge.addProperty("end", end);
        edge.addProperty("label", edgeLabel);
        return edge;
    }

    private String renderDataLiteral(IRI iri, OWLLiteral object) {
        final StringBuilder sb = new StringBuilder();
        sb.append(iri.getShortForm());
        sb.append(" ");
        sb.append(object.getLiteral());
        return sb.toString();
    }

    private JsonObject buildEdges(OWLObject startNode, OWLObject endNode, String edgeLabel) {
        final JsonObject edge = new JsonObject();
        edge.addProperty("start", renderer.render(startNode));
        edge.addProperty("end", renderer.render(endNode));
        edge.addProperty("label", edgeLabel);
        return edge;
    }

    public JsonObject getDataSet(OWLClass clzz) {
        getDataSuggestionByClass(clzz);
        return dataSet;
    }

    protected Multimap<OWLDataProperty, Multimap<Object, Set<SWRLAtom>>> getDataSuggestionByClass(OWLClass clzz) {
        nodes.add(buildOWLObjectNode(clzz));
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
                        nodes.add(buildDataNode(renderer.render(node.getLiteral())));
                        edges.add(buildDataEdge(renderer.render(clzz), renderer.render(node.getLiteral()), renderer.render(propertyAtom.getPredicate())));
                        extractClassFromRuleHead(rule).forEach(cls -> {
                            nodes.add(buildOWLObjectNode(cls));
                            edges.add(buildDataEdge(renderer.render(node.getLiteral()), renderer.render(cls), "maybe a"));
                        });
                    }

                    @Override
                    public void visit(SWRLVariable variable) {
                        Map<IRI, OWLLiteral> variableDependencies = collectLiteralEndpoint(rule, variable);
                        if (!variableDependencies.isEmpty())
                            variableDependencies.entrySet().forEach(entry -> {
                                nodes.add(buildDataNode(renderDataLiteral(entry.getKey(), entry.getValue())));
                                edges.add(buildDataEdge(
                                        renderer.render(clzz), renderDataLiteral(entry.getKey(), entry.getValue()),
                                        renderer.render(propertyAtom.getPredicate())));
                                extractClassFromRuleHead(rule).forEach(cls -> {
                                    nodes.add(buildOWLObjectNode(cls));
                                    edges.add(buildDataEdge(
                                            renderDataLiteral(entry.getKey(), entry.getValue()),
                                            renderer.render(cls), "maybe a"));
                                });
                            });
                        consequence.put(variableDependencies, rule.getHead());

                    }
                });
                dataPropertySuggestions.put(propertyAtom.getPredicate().asOWLDataProperty(), consequence);
            }
        })));
        return dataPropertySuggestions;
    }

    private Set<OWLClass> extractClassFromRuleHead(SWRLRule rule) {
        final Set<OWLClass> retClzz = new HashSet<>();
        rule.getHead().stream()
                .filter(atom -> (atom instanceof SWRLClassAtom))
                .map(atom -> ((SWRLClassAtom) atom))
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(clsAtom -> retClzz.add(clsAtom.getPredicate().asOWLClass()));

        return retClzz;
    }
}
