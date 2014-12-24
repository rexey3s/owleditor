package vn.edu.uit.owleditor.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.view.demo.JSDiagram;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/23/14.
 */
public class DiagramSheet extends VerticalLayout {
    private static final int SIZE = 400;
    private static Logger LOG = LoggerFactory.getLogger(JSDiagram.class);
    private final JsonObject thingObject = new JsonObject();
    private final JsonArray thingArray = new JsonArray();
    private final Set<OWLClass> visited = new HashSet<>();
    public DiagramSheet() {
        OWLEditorKit editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        thingObject.addProperty("name", "Thing");
        thingObject.add("children", thingArray);
        editorKit.getActiveOntology().accept(initPopulationEngine(editorKit.getActiveOntology()));
        FileOutputStream fos;
        File file;
        try {
            file = new File("src/main/java/vn/edu/uit/owleditor/view/demo/hierarchy.json");
            fos = new FileOutputStream(file.getAbsoluteFile());
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            Gson gson = new GsonBuilder().create();
            gson.toJson(thingObject, writer);

        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

        JSDiagram diagram = new JSDiagram();
        final HorizontalLayout diagramContainer = new HorizontalLayout();
        addStyleName("diagram-container");
        diagramContainer.addComponent(diagram);
        diagramContainer.setSizeFull();
        addComponent(diagramContainer);
        setSizeFull();
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
}
