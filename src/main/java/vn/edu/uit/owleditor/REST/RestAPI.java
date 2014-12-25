package vn.edu.uit.owleditor.REST;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.vaadin.spring.events.*;
import vn.edu.uit.owleditor.core.OWLEditorKit;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/23/14.
 */
@RestController
public class RestAPI implements EventBusListener<Object> {
    private static final int SIZE = 400;
    private static final Logger LOG = LoggerFactory.getLogger(RestAPI.class);
    private final JsonObject thingObject = new JsonObject();
    private final JsonArray thingArray = new JsonArray();
    private final Set<OWLClass> visited = new HashSet<>();

    @Autowired
    @EventBusScope(value = EventScope.SESSION, proxy = true)
    EventBus eventBus;

    
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    @RequestMapping(value = "/api/hierarchy", method = RequestMethod.GET)
    public
    @ResponseBody
    String getHierarchy() {
//        OWLEditorKit editorKit = (OWLEditorKit) session.getAttribute("kit");
//        LOG.info(editorKit.getActiveOntology().toString());
        try {
            eventBus.subscribe(this);
            thingObject.addProperty("name", "Thing");
            thingObject.add("children", thingArray);
//            ontology.accept(initPopulationEngine(ontology));
            return thingObject.toString();
        } catch (NullPointerException ex) {
            LOG.error(ex.getMessage());
        }
        return "No ontology was found";
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

    @Override
    public void onEvent(Event<Object> event) {
        OWLOntology ontology = (OWLOntology) event.getPayload();
        LOG.info("Ontology " + ontology.toString());
    }
}
