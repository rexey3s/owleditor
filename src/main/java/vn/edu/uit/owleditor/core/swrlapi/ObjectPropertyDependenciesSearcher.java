package vn.edu.uit.owleditor.core.swrlapi;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.*;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/29/14.
 */
public class ObjectPropertyDependenciesSearcher {
    private static OWLObjectRenderer renderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
    private final Multimap<OWLObjectProperty, Multimap<Set<OWLClass>, Set<SWRLAtom>>> objectPropertiesSuggestion = LinkedListMultimap.create();
    private final Set<SWRLAPIRule> rules = new HashSet<>();
    private final JsonObject dataSet = new JsonObject();
    private JsonArray nodes = new JsonArray();
    private JsonArray edges = new JsonArray();

    public ObjectPropertyDependenciesSearcher(SWRLAPIOWLOntology swrlapiowlOntology) {
        swrlapiowlOntology.getSWRLRules().stream()
                .filter(rule -> !rule.isSQWRLQuery())
                .forEach(rules::add);
        dataSet.add("nodes", nodes);
        dataSet.add("edges", edges);
    }

    private boolean checkAtomContainClassInSignatures(SWRLAtom atom, OWLClass owlClass) {
        return atom.getPredicate() instanceof OWLClass && ((OWLClass) atom.getPredicate()).equals(owlClass);
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
                if (propertyAtom.getSecondArgument().equals(classAtom.getArgument())
                        && !classAtom.getPredicate().isAnonymous()) {
                    classesSuggestion.add(classAtom.getPredicate().asOWLClass());

                }
            }
        });
        return classesSuggestion;
    }
//    private JsonObject buildDataNode(OWLLiteral literal) {
//        final JsonObject jsonLiteral = new JsonObject();
//        final String shortForm = OWLEditorKitImpl.render(literal);
//        
//        
//    }

    private JsonObject buildEntityNode(OWLEntity entity) {
        final JsonObject jsonEntity = new JsonObject();
        final String shortForm = renderer.render(entity);
        jsonEntity.addProperty("id", shortForm);
        jsonEntity.addProperty("label", shortForm);
        jsonEntity.addProperty("type", entity.getEntityType().toString());
        return jsonEntity;
    }

    //    private JsonObject buildMediumNode(String id) {
//        final JsonObject json = new JsonObject();
//        json.addProperty("id", id);
//        json.addProperty("label", id);
//        return json;
//    }
//
//    private JsonObject buildMediumEdge(String start, String end, String label) {
//        final JsonObject edge = new JsonObject();
//        edge.addProperty("start", start);
//        edge.addProperty("end", end);
//        edge.addProperty("label", label);
//        return edge;
//    }
//
    private JsonObject buildEdges(OWLEntity startNode, OWLEntity endNode, String edgeLabel) {
        final JsonObject edge = new JsonObject();
        edge.addProperty("start", renderer.render(startNode));
        edge.addProperty("end", renderer.render(endNode));
        edge.addProperty("label", edgeLabel);
        return edge;
    }

    protected Multimap<OWLObjectProperty, Multimap<Set<OWLClass>, Set<SWRLAtom>>> getObjectPropertySuggestionByClass(OWLClass clzz) {
        nodes.add(buildEntityNode(clzz));
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
                final Set<OWLClass> endpoints = collectObjectPropertyEndpoint(rule, propertyAtom);

                endpoints.forEach(clz -> {
                    nodes.add(buildEntityNode(clz));
                    edges.add(buildEdges(clzz, clz, renderer.render(propertyAtom.getPredicate())));
                    extractClassFromRuleHead(rule).forEach(result -> {
                        nodes.add(buildEntityNode(result));
                        edges.add(buildEdges(clz, result, "có thể là"));
                    });
                });

                consequence.put(endpoints, rule.getHead());
                objectPropertiesSuggestion.put(propertyAtom.getPredicate().asOWLObjectProperty(), consequence);
            }
        })));

        return objectPropertiesSuggestion;
    }

    public JsonObject getDataSet(OWLClass clzz) {
        getObjectPropertySuggestionByClass(clzz);
        return dataSet;
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
