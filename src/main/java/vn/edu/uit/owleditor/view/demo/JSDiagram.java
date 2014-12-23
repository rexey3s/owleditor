package vn.edu.uit.owleditor.view.demo;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import org.json.JSONObject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKit;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/21/14.
 */
@JavaScript({"https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js", "http://d3js.org/d3.v3.min.js", "hierarchy.js", "hierarchy.json"})
public class JSDiagram extends AbstractJavaScriptComponent {
    private static final int SIZE = 400;
    private final JsonObject thingObject = new JsonObject();
    private final JsonArray thingArray = new JsonArray();
    private final Set<OWLClass> visited = new HashSet<>();
    private final OWLOntology activeOntology;

    public JSDiagram(OWLOntology ontology) {
        this.activeOntology = ontology;
        thingObject.addProperty("name", "Thing");
        thingObject.add("children", thingArray);
        ontology.accept(initPopulationEngine(activeOntology));
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("hierachy.json"), "UTF-8")) {

            Gson gson = new GsonBuilder().create();
            gson.toJson(thingObject, writer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void recursive(OWLOntology ontology, OWLClass child, OWLClass parent, JsonObject parentObject) {
        visited.add(child);
        final JsonObject childObject = new JsonObject();
        childObject.addProperty("name", OWLEditorKit.getShortForm(child));
        childObject.addProperty("size", randInt(2, 10) * SIZE);

        if (parent != null && parentObject.has("children")) {

            parentObject.get("children").getAsJsonArray().add(childObject);

        } else {

            if (EntitySearcher.getSuperClasses(child, ontology).size() == 0)
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

    private OWLObjectVisitorAdapter initPopulationEngine(OWLOntology activeOntology) {
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
                    recursive(activeOntology, owlClass, null, null);
                }
            }
        };
    }
    public String getValue() {
        return getState().value;
    }

    public void setValue(String value) {
        getState().value = value;
    }

    public void setData(JSONObject data) {
        getState().data = data;
    }


    @Override
    protected JSDiagramState getState() {
        return (JSDiagramState) super.getState();
    }
}
