package vn.edu.uit.owleditor.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.springframework.util.Assert;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 1/29/15.
 */
public class ClassHierarchicalTree {
    private static final int SIZE = 400;
    protected final Set<OWLClass> visited = new HashSet<>();
    private final JsonObject thingObject = new JsonObject();
    OWLEditorKit editorKit;
    private JsonArray thingArray;

    public ClassHierarchicalTree(OWLEditorKit eKit) {


    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @PostConstruct
    protected void postConstruct() {
        thingObject.addProperty("name", "Thing");
        thingObject.add("children", thingArray);
        thingArray = new JsonArray();
    }

    public void reload(OWLOntology ontology) {
        visited.clear();
        thingObject.remove("children");
        thingArray = new JsonArray();
        thingObject.add("children", thingArray);
        ontology.accept(initPopulationEngine(ontology, thingObject));
    }


    public String refreshTree() {
        try {
            Assert.notNull(editorKit, "Editor Kit should not be null");
            Assert.notNull(editorKit.getActiveOntology(), "Your ontology has not been ready yet!");
            reload(editorKit.getActiveOntology());
            return thingObject.toString();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return thingObject.toString();
    }


    private void recursive(OWLOntology ontology, OWLClass child, OWLClass parent, JsonObject parentObject) {
        visited.add(child);
        final JsonObject childObject = new JsonObject();
        childObject.addProperty("name", OWLEditorKitImpl.getShortForm(child));
        childObject.addProperty("size", randInt(2, 10) * SIZE);

        if (parent != null && parentObject.has("children")) {

            parentObject.get("children").getAsJsonArray().add(childObject);

        } else {
            Collection<OWLClass> superClasses = EntitySearcher
                    .getSuperClasses(child, ontology)
                    .stream()
                    .filter(ce -> (ce instanceof OWLClass))
                    .map(OWLClassExpression::asOWLClass)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (superClasses.size() == 0)
                thingArray.add(childObject);
        }
        Collection<OWLClassExpression> childs = EntitySearcher.getSubClasses(child, ontology);
        if (childs.size() > 0) {
            childObject.remove("size");
            final JsonArray childArray = new JsonArray();
            childObject.add("children", childArray);
            childs.forEach(
                    ce -> ce.accept(new OWLClassExpressionVisitorAdapter() {
                        public void visit(OWLClass owlClass) {
                            recursive(ontology, owlClass, child, childObject);
                        }
                    })
            );
        }
    }

    private OWLObjectVisitor initPopulationEngine(OWLOntology activeOntology, JsonObject top) {
        return new OWLObjectVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLOntology ontology) {
                ontology.getClassesInSignature()
                        .stream().filter(c -> !c.isOWLThing())
                        .forEach(c -> c.accept(this));
            }

            @Override
            public void visit(@Nonnull OWLClass owlClass) {
                if (!visited.contains(owlClass)) {
                    recursive(activeOntology, owlClass, null, top);
                }
            }
        };
    }
}
